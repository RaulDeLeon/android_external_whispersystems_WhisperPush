package org.whispersystems.whisperpush.loaders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import org.whispersystems.textsecure.util.PhoneNumberFormatter;

/**
 * Loader that builds a list of all the countries and their respective country codes
 * supported by libphonenumber.
 */
public class CountryListLoader extends AsyncTaskLoader<ArrayList<Map<String, String>>> {

  public CountryListLoader(Context context) {
    super(context);
  }

  @Override
  public ArrayList<Map<String, String>> loadInBackground() {
    Set<String> regions                    = PhoneNumberUtil.getInstance().getSupportedRegions();
    ArrayList<Map<String, String>> results = new ArrayList<Map<String, String>>(regions.size());

    for (String region : regions) {
      Map<String, String> data = new HashMap<String, String>(2);
      data.put("country_name", PhoneNumberFormatter.getRegionDisplayName(region));
      data.put("country_code", "+" +PhoneNumberUtil.getInstance().getCountryCodeForRegion(region));
      results.add(data);
    }

    Collections.sort(results, new RegionComparator());

    return results;
  }

  private class RegionComparator implements Comparator<Map<String, String>> {
    @Override
    public int compare(Map<String, String> lhs, Map<String, String> rhs) {
      return lhs.get("country_name").compareTo(rhs.get("country_name"));
    }
  }
}
