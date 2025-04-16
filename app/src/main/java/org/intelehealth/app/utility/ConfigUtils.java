package org.intelehealth.app.utility;

import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ConfigUtils {
    public static String TAG = ConfigUtils.class.getSimpleName();
    Context context;

    public ConfigUtils(Context context) {
        this.context = context;
    }

    private JSONObject jsonreader() {
//        sessionManager = new SessionManager(IntelehealthApplication.getAppContext());
        JSONObject obj = null;
        try {
//            if (sessionManager.valueContains("licensekey")) {
            //NonNull added to handle null values in case of downloaded mm's.
            //Load the config file
//            if (!sessionManager.getLicenseKey().isEmpty())
//                obj = new JSONObject(Objects.requireNonNullElse
//                        (FileUtils.readFileRoot(AppConstantsKt.CONFIG_FILE_NAME, context),
//                                String.valueOf(FileUtils.encodeJSON(context, AppConstantsKt.CONFIG_FILE_NAME))));
//            else
                obj = new JSONObject(String.valueOf(FileUtils.encodeJSON(context, AppConstantsKt.CONFIG_FILE_NAME)));

        } catch (JSONException e) {
            Toast.makeText(context, "JsonException" + e, Toast.LENGTH_LONG).show();
        }
        return obj;
    }


    public boolean height() {
        boolean view = false;

        JSONObject object = jsonreader();
        try {
            view = object.getBoolean("mHeight");
        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return view;
    }

    public boolean weight() {
        boolean view = false;

        JSONObject object = jsonreader();
        try {
            view = object.getBoolean("mWeight");
        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return view;
    }


    public boolean temperature() {
        boolean view = false;

        JSONObject object = jsonreader();
        try {
            view = object.getBoolean("mTemperature");
        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return view;
    }

    public boolean celsius() {
        boolean view = false;

        JSONObject object = jsonreader();
        try {
            view = object.getBoolean("mCelsius");
        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return view;
    }

    public boolean fahrenheit() {
        boolean view = false;

        JSONObject object = jsonreader();
        try {
            view = object.getBoolean("mFahrenheit");
        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return view;
    }

    public boolean privacy_notice() {
        boolean view = false;

        JSONObject object = jsonreader();
        try {
            view = object.getBoolean("privacyNotice");
        } catch (JSONException | NullPointerException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return view;
    }

    // check with the package name
    // if app is available or not
    private boolean checkAppAvailable(Context context, String name) {
        boolean available = true;
        try {
            // check if available
            context.getPackageManager().getPackageInfo(name, 0);
        } catch (PackageManager.NameNotFoundException e) {
            available = false;
        }
        return available;
    }

    public String getTeleconsultationConsentText(String locale) {
        String val = "";

        JSONObject object = jsonreader();
        try {
            val = object.getString("teleconsultation_consent_" + locale);

        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return val;
    }

    public String getPrivacyPolicyText(String locale) {
        String val = "";

        JSONObject object = jsonreader();
        try {
            val = object.getString("privacy_policy_" + locale);

        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return val;
    }

    public String getTermsAndConditionsText(String locale) {
        String val = "";

        JSONObject object = jsonreader();
        try {
            val = object.getString("terms_and_conditions_" + locale);

        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return val;
    }

    public String getPersonalDataConsentText(String locale) {
        String val = "";

        JSONObject object = jsonreader();
        try {
            val = object.getString("personalDataConsentText_" + locale);

        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return val;
    }

}
