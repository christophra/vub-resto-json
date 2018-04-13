package be.christophedetroyer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    static String DUTCH_URL = "https://student.vub.be/menu-vub-student-restaurant";
    static String ENGLISH_URL = "https://student.vub.be/en/menu-vub-student-restaurant";

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

        /////////////////////
        // Dutch Etterbeek //
        /////////////////////

        Document doc_nl = Jsoup.connect(DUTCH_URL).get();

        List<JSONObject> week_etterbeek_nl = new ArrayList<JSONObject>(5);

        Element etterbeek_nl_monday = doc_nl.selectFirst("section:has(h2:contains(Weekmenu Etterbeek)) + section");
        JSONObject etterbeek_nl_monday_json = parseDay(etterbeek_nl_monday);
        week_etterbeek_nl.add(etterbeek_nl_monday_json);

        Element etterbeek_nl_tuesday = doc_nl.selectFirst("section:has(h2:contains(Weekmenu Etterbeek)) + section + section");
        JSONObject etterbeek_nl_tuesday_json = parseDay(etterbeek_nl_tuesday);
        week_etterbeek_nl.add(etterbeek_nl_tuesday_json);

        Element etterbeek_nl_wednesday = doc_nl.selectFirst("section:has(h2:contains(Weekmenu Etterbeek)) + section + section + section");
        JSONObject etterbeek_nl_wednesday_json = parseDay(etterbeek_nl_wednesday);
        week_etterbeek_nl.add(etterbeek_nl_wednesday_json);

        Element etterbeek_nl_thursday = doc_nl.selectFirst("section:has(h2:contains(Weekmenu Etterbeek)) + section + section + section + section");
        JSONObject etterbeek_nl_thursday_json = parseDay(etterbeek_nl_thursday);
        week_etterbeek_nl.add(etterbeek_nl_thursday_json);

        Element etterbeek_nl_friday = doc_nl.selectFirst("section:has(h2:contains(Weekmenu Etterbeek)) + section + section + section + section + section");
        JSONObject etterbeek_nl_friday_json = parseDay(etterbeek_nl_friday);
        week_etterbeek_nl.add(etterbeek_nl_friday_json);

        // Put them in their file.
        String etterbeekfilenl = String.format("%setterbeek.nl.json", outputpath);
        writeToFile(etterbeekfilenl, new JSONArray(week_etterbeek_nl).toString());

        ////////////////////////
        // English Etterbeek //
        ///////////////////////

        List<JSONObject> week_jette_nl = new ArrayList<JSONObject>(5);

        Element jette_nl_monday = doc_nl.selectFirst("section:has(h2:contains(Weekmenu Jette)) + section");
        JSONObject jette_nl_monday_json = parseDay(jette_nl_monday);
        week_jette_nl.add(jette_nl_monday_json);

        Element jette_nl_tuesday = doc_nl.selectFirst("section:has(h2:contains(Weekmenu Jette)) + section + section");
        JSONObject jette_nl_tuesday_json = parseDay(jette_nl_tuesday);
        week_jette_nl.add(jette_nl_tuesday_json);

        Element jette_nl_wednesday = doc_nl.selectFirst("section:has(h2:contains(Weekmenu Jette)) + section + section + section");
        JSONObject jette_nl_wednesday_json = parseDay(jette_nl_wednesday);
        week_jette_nl.add(jette_nl_wednesday_json);

        Element jette_nl_thursday = doc_nl.selectFirst("section:has(h2:contains(Weekmenu Jette)) + section + section + section + section");
        JSONObject jette_nl_thursday_json = parseDay(jette_nl_thursday);
        week_jette_nl.add(jette_nl_thursday_json);

        Element jette_nl_friday = doc_nl.selectFirst("section:has(h2:contains(Weekmenu Jette)) + section + section + section + section + section");
        JSONObject jette_nl_friday_json = parseDay(jette_nl_friday);
        week_jette_nl.add(jette_nl_friday_json);

        // Put them in their file.
        String jettefilenl = String.format("%sjette.nl.json", outputpath);
        writeToFile(jettefilenl, new JSONArray(week_jette_nl).toString());


        ///////////////////////
        // Etterbeek English //
        ///////////////////////

        Document doc_en = Jsoup.connect(ENGLISH_URL).get();

        List<JSONObject> week_etterbeek_en = new ArrayList<JSONObject>(5);

        Element etterbeek_en_monday = doc_en.selectFirst("section:has(h2:contains(Week menu Etterbeek)) + section");
        JSONObject etterbeek_en_monday_json = parseDay(etterbeek_en_monday);
        week_etterbeek_en.add(etterbeek_en_monday_json);

        Element etterbeek_en_tuesday = doc_en.selectFirst("section:has(h2:contains(Week Menu Etterbeek)) + section + section");
        JSONObject etterbeek_en_tuesday_json = parseDay(etterbeek_en_tuesday);
        week_etterbeek_en.add(etterbeek_en_tuesday_json);

        Element etterbeek_en_wednesday = doc_en.selectFirst("section:has(h2:contains(Week Menu Etterbeek)) + section + section + section");
        JSONObject etterbeek_en_wednesday_json = parseDay(etterbeek_en_wednesday);
        week_etterbeek_en.add(etterbeek_en_wednesday_json);

        Element etterbeek_en_thursday = doc_en.selectFirst("section:has(h2:contains(Week Menu Etterbeek)) + section + section + section + section");
        JSONObject etterbeek_en_thursday_json = parseDay(etterbeek_en_thursday);
        week_etterbeek_en.add(etterbeek_en_thursday_json);

        Element etterbeek_en_friday = doc_en.selectFirst("section:has(h2:contains(Week Menu Etterbeek)) + section + section + section + section + section");
        JSONObject etterbeek_en_friday_json = parseDay(etterbeek_en_friday);
        week_etterbeek_en.add(etterbeek_en_friday_json);

        // Put them in their file.
        String etterbeekfileen = String.format("%setterbeek.en.json", outputpath);
        writeToFile(etterbeekfileen, new JSONArray(week_etterbeek_en).toString());


        //////////////////////////
        // Etterbeek Nederlands //
        //////////////////////////

        List<JSONObject> week_jette_en = new ArrayList<JSONObject>(5);

        Element jette_en_monday = doc_en.selectFirst("section:has(h2:contains(Week menu Jette)) + section");
        JSONObject jette_en_monday_json = parseDay(jette_en_monday);
        week_jette_en.add(jette_en_monday_json);

        Element jette_en_tuesday = doc_en.selectFirst("section:has(h2:contains(Week Menu Jette)) + section + section");
        JSONObject jette_en_tuesday_json = parseDay(jette_en_tuesday);
        week_jette_en.add(jette_en_tuesday_json);

        Element jette_en_wednesday = doc_en.selectFirst("section:has(h2:contains(Week Menu Jette)) + section + section + section");
        JSONObject jette_en_wednesday_json = parseDay(jette_en_wednesday);
        week_jette_en.add(jette_en_wednesday_json);

        Element jette_en_thursday = doc_en.selectFirst("section:has(h2:contains(Week Menu Jette)) + section + section + section + section");
        JSONObject jette_en_thursday_json = parseDay(jette_en_thursday);
        week_jette_en.add(jette_en_thursday_json);

        Element jette_en_friday = doc_en.selectFirst("section:has(h2:contains(Week Menu Jette)) + section + section + section + section + section");
        JSONObject jette_en_friday_json = parseDay(jette_en_friday);
        week_jette_en.add(jette_en_friday_json);

        // Put them in their file.
        String jettefileen = String.format("%sjette.en.json", outputpath);
        writeToFile(jettefileen, new JSONArray(week_jette_en).toString());
        return;

    }

    //    <section class="pg-text rd-content js-extra ">
    //        <div class="rd-container">
    //            <div class="rd-content-holder js-extra-content">
    //                <h4>Maandag</h4>
    //                <p>02.04.2018: Paasmaandag gesloten</p>
    //                <ul>
    //                    <li>Soep:</li>
    //                    <li>Menu 1:</li>
    //                    <li>Menu 2:</li>
    //                    <li>Vis:</li>
    //                    <li>Veggie:</li>
    //                    <li>Pasta bar:</li>
    //                    <li>Wok:</li>
    //                </ul>
    //            </div>
    //        </div>
    //    </section>
    public static JSONObject parseDay(Element section) {
        JSONObject day_json = new JSONObject();

        String date = section.selectFirst("div > div > p").text().trim().replace(":", "").toLowerCase();
        String[] date_parts = date.split("\\.");

        String day_int = date_parts[0].trim();
        String month_int = date_parts[1].trim();
        String year_int = date_parts[2].substring(0, 4); // In case there is an additional message after the date (see example).
        String day_string = String.format("%s-%s-%s", day_int, month_int, year_int);

        Elements foods = section.select("li");


        List<JSONObject> dishes_json = new LinkedList<JSONObject>();
        for (Element food : foods) {
            String raw_text = food.text();
            String[] tokens = raw_text.split(":");

            String name = tokens[0];


            // If the dish is empty, it's not there.
            if (tokens.length > 1) {
                String dish = formatDish(String.join(" ", Arrays.copyOfRange(tokens, 1, tokens.length)));
                JSONObject dish_json = new JSONObject();
                dish_json.put("name", name);
                dish_json.put("dish", dish);
                dish_json.put("color", COLORMAP.getOrDefault(name.toLowerCase(), "#fdb85b"));

                dishes_json.add(dish_json);
            }
        }
        day_json.put("menus", dishes_json);
        day_json.put("date", day_string);
        return day_json;

    }


    public static String formatDish(String name) {
        String lower = name.toLowerCase().trim();

        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
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

        if (!f.exists()) {
            f.createNewFile();
        }

        BufferedWriter out = new BufferedWriter(new FileWriter(filename));
        out.write(output);
        out.flush();
    }


}
