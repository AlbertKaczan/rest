import openconnector.*;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONException;
import org.richfaces.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static sailpoint.connector.nidm.NIDMConnector.ATTRIBUTES;
import static sailpoint.connector.nidm.NIDMConnector.USER_KEY;

public class RestConnector extends AbstractRestConnectorV2 {
    ////////////////////////////////////////////////////////////////////////////
    //
    // CONSTANTS
    //
    ////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////
    //
    // CONSTRUCTORS
    //
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Default constructor.
     */
    public RestConnector() {
        super();
        setAttributesAndFunctions();
    }

    /**
     * Constructor for an account CustomConnectorTest
     *
     * @param config The ConnectorConfig to use.
     * @param log    The Log to use.
     */
    public RestConnector(ConnectorConfig config, Log log) {
        super(config, log);
        setAttributesAndFunctions();
    }


    /**
     * Hardcoded debug constructor.
     */
    public RestConnector(String configUserName, String configUserPassword, String configUrl) {
        super(configUserName, configUserPassword, configUrl);

        super.configUserName = configUserName;
        super.configUserPassword = configUserPassword;
        super.configUrl = configUrl;
        super.hardcoded = true;


    }

    ////////////////////////////////////////////////////////////////////////////
    //
    // INIT
    //
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void init() {
        final String funcName = "init";

        logEnter(funcName);

        if (!hardcoded) {
            logDebug("Initialising...");

            configUserName = config.getString("user");
            configUserPassword = config.getString("password");
            configUrl = config.getString("url");
        }

        logExit(funcName);
    }
    @Override
    protected HttpURLConnection getGetConnectionWithLocation(String location) throws IOException{
        return getGetConnection(new URL(configUrl + location));

    }
    @Override
    public HttpURLConnection getConnection (URL url) throws IOException{
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        return conn;
    }

    @Override
    public void setAttributesAndFunctions() {
        getAllUserAddress = "/Identitator2000/";

        String[] Acat = {"firstName", "lastName", "id", "email"};
        setAttributesAccount(Acat);

    }

    @Override
    protected void setAttributesAccount(String[] Atributes){
        attributesAccount = Atributes;
    }
    @Override
    public List<Feature> getSupportedFeatures(String objectType) {
        List<Feature> res = new ArrayList<>();

        return res;
    }

    ////////////////////////////////////////////////////////////////////////////
    //
    // BASIC CRUD
    //
    ////////////////////////////////////////////////////////////////////////////

    @Override
    protected String getJsonDataAsString(String loc) throws IOException{
        HttpURLConnection conn = getConnection(getAllUserAddress + loc);

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "applicationa/json");

        checkConnection(conn);

        String jsonData = connectionToString(conn);

        System.out.println(jsonData);
        //AccountAttributesList.append

        conn.disconnect();
        return jsonData.toString();
    }

    @Override
    public Map<String,Map<String,Object>> getObjectsMap() {
        Map<String, Map<String, Object>> res;
        try {
            if (OBJECT_TYPE_ACCOUNT.equals(this.objectType)) {
                checkIsNull(attributesAccount, "ATTRIBUTES_ACCOUNT");

                JSONArray jsArray = getJsonUserDataAsJSONArray();

                res = createObjectsFromJSONArray(jsArray, attributesAccount);
            } else
                throw new ConnectorException(ERROR_MSG_UNKNOWN_OBJECT + this.objectType);
            } catch (Exception e) {
            String err = exceptionToString(e);

            throw new ConnectorException(e.getMessage(), e);

        }

        return res;
    }

    @Override
    protected Map<String, Object> getUser(String nativeIdentifier) throws IOException, JSONException {

        return getObjectsMap().get(nativeIdentifier);
    }

    @Override
    protected void createUser(Map<String, Object> user) throws ClassNotFoundException, IOException, JSONException {

    }

    @Override
    protected void updateUser(String id, Map<String, Object> userUpdateMap) throws ClassNotFoundException, IOException {

    }

    @Override
    protected void updateSetPassword(String id, Map<String, Object> user) throws ClassNotFoundException, IOException {

    }

    @Override
    protected void deleteUser(String id) throws ClassNotFoundException, IOException {

    }

    ////////////////////////////////////////////////////////////////////////////
    //
    // OVERRIDDEN
    //
    ////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////
    //
    // GETTERS AND SETTERS
    //
    ////////////////////////////////////////////////////////////////////////////

}
