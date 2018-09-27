package qa.challenge.autohero;

import java.io.IOException;
import java.util.Properties;

public class PageProperties {

    private static PageProperties INSTANCE;
    private static final String SEARCH_PAGE_KEY = "search";
    private static final String PROPERTIES_FILE_NAME = "/pageUrl.properties";

    private final Properties properties;

    private PageProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(this.getClass().getResourceAsStream(PROPERTIES_FILE_NAME));
        this.properties = new Properties(properties);
    }

    private static synchronized PageProperties getINSTANCE() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new PageProperties();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    public static String getSearchPageUrl() {
        return getINSTANCE().properties.getProperty(SEARCH_PAGE_KEY);
    }


}
