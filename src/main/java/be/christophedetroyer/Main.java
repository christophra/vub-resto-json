package be.christophedetroyer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.*;

public class Main {
    static String DUTCH_URL = "https://student.vub.be/resto";
    static String ENGLISH_URL = "https://student.vub.be/en/food-and-drinks";

    private static final Map<String, String> COLORMAP;

    static {
        COLORMAP = new HashMap<String, String>();
        COLORMAP.put("soep", "#fdb85b");
        COLORMAP.put("soup", "#fdb85b");
        COLORMAP.put("menu 1", "#68b6f3");
        COLORMAP.put("dag menu", "#68b6f3");
        COLORMAP.put("dagmenu", "#68b6f3");
        COLORMAP.put("health", "#ff9861");
        COLORMAP.put("menu 2", "#cc93d5");
        COLORMAP.put("meals of the world", "#cc93d5");
        COLORMAP.put("fairtrade", "#cc93d5");
        COLORMAP.put("fairtrade menu", "#cc93d5");
        COLORMAP.put("veggie", "#87b164");
        COLORMAP.put("veggiedag", "#87b164");
        COLORMAP.put("pasta", "#de694a");
        COLORMAP.put("pasta bar", "#de694a");
        COLORMAP.put("wok", "#6c4c42");
    }


    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Expected one argument: the path to write the files to.");
            return;
        }

        // The program expects on parameter, namely the directory where to write the files.
        final String outputpath = args[0];


        // Get the source for the dutch site, and extract etterbeek and jette.
        Document doc_nl = Jsoup.connect(DUTCH_URL).get();
        Document doc_en = Jsoup.connect(ENGLISH_URL).get();
        Element etterbeekSection_nl = doc_nl.selectFirst("section:has(h2:contains(Menu Etterbeek)) + section");
        Element etterbeekSection_en = doc_en.selectFirst("section:has(h2:contains(What\\\'s for lunch in Etterbeek)) + section");
        Element jetteSection_nl = doc_nl.selectFirst("section:has(h2:contains(Menu Jette)) + section");
        Element jetteSection_en = doc_en.selectFirst("section:has(h2:contains(What\\\'s for lunch in Jette)) + section");

        List<JSONObject> etterbeek_nl = getJsonObjects(etterbeekSection_nl);
        String etterbeekfilenl = String.format("%setterbeek.nl.json", outputpath);
        writeToFile(etterbeekfilenl, new JSONArray(etterbeek_nl).toString());

        List<JSONObject> etterbeek_en = getJsonObjects(etterbeekSection_en);
        String etterbeekfileen = String.format("%setterbeek.en.json", outputpath);
        writeToFile(etterbeekfileen, new JSONArray(etterbeek_en).toString());

        List<JSONObject> jette_en = getJsonObjects(jetteSection_en);
        String jettefileen = String.format("%sjette.en.json", outputpath);
        writeToFile(jettefileen, new JSONArray(jette_en).toString());

        List<JSONObject> jette_nl = getJsonObjects(jetteSection_nl);
        String jettefilenl = String.format("%sjette.nl.json", outputpath);
        writeToFile(jettefilenl, new JSONArray(jette_nl).toString());
    }

    /**
     * Writes a given string to a given file.
     *
     * @param filename Absolute file path..
     * @param output   String to write.
     * @throws IOException
     */
    private static void writeToFile(String filename, String output) throws IOException {

        File f = new File(filename);

        if(!f.exists())
        {
            f.createNewFile();
        }

        BufferedWriter out = new BufferedWriter(new FileWriter(filename));
        out.write(output);
        out.flush();
    }

    /**
     * Given the section, parses it into a list of json objects. Each representing a menu of the day.
     *
     * @param section
     * @return A list of json objects representing a day of the week with its menu.
     */
    private static List<JSONObject> getJsonObjects(Element section) {
        Element table = section.selectFirst("table");
        Elements rows = table.select("tr:not(tr:has(th))").select("tr:has(td:matches(.+))");
        Elements days = table.select("th:matches(.+)");
        List<JSONObject> dayjson = new ArrayList<JSONObject>();


        for (int i = 0; i < days.size(); i++) {
            Element day = days.get(i);

            JSONObject json = new JSONObject();

            // Build the date String.
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            String day_raw = day.text().trim();
            String[] parts = day_raw.split("/");
            String day_str = parts[0];
            String mth_str = parts[1];
            String yer_str = currentYear + "";
            String output_str = String.format("%s-%s-%s", yer_str, mth_str, day_str);

            json.put("date", output_str);

            List<JSONObject> menus = new LinkedList<JSONObject>();

            // Get the dishes from the rows.
            for (int r = 0; r < rows.size(); r++) {
                JSONObject menu = new JSONObject();

                Element row = rows.get(r);
                Elements datas = row.select("td:matches(.+)");

                String dish = datas.get(0).text().trim();
                String name = datas.get(i + 1).text().trim();
                String color = COLORMAP.getOrDefault(dish.toLowerCase(), "#fdb85b");

                menu.put("name", dish);
                menu.put("dish", name);
                menu.put("color", color);

                menus.add(menu);

            }

            json.put("menus", menus);
            dayjson.add(json);

        }
        return dayjson;
    }
}
