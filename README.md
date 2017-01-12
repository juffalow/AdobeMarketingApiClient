# Adobe Marketing API Client in Java

## Note

The only thing that is done is UploadData method from DataSources API. But it can
be used as artwork how the API is working and how you can communicate with it
using java language.

## Dependencies

The project has one dependence : [google-gson](https://github.com/google/gson).

It is used to return JSON string in [UploadDataRequest](/src/com/juffalow/adobemarketingapiclient/datasources/UploadDataRequest.java).

## Example

```
package whatever;

import com.juffalow.adobemarketingapiclient.AdobeMarketingApiClient;
import com.juffalow.adobemarketingapiclient.datasources.UploadData;

public class Upload {
    public static void main(String[] args) {
        AdobeMarketingApiClient amac = new AdobeMarketingApiClient(<username>, <secret>);
        try {
            UploadData ud = (UploadData) amac.getDataSourcesMethod("UploadData", null, null);

            ud.setColumns(new String[]{"Date", "Evar X", "Event X"});
            ud.setDataSourceId(<data source ID>);
            ud.setReportSuiteId(<report suite ID>);


            for( int i = 0; i < <rows count>; i++ ) {
                ud.addRow(new String[]{<timestamp>, <Evar X value>, <Event X value>});
            }

            ud.finish();
        } catch( Exception exception ) {
            exception.printStackTrace();
        }
    }
}
```

## Links

[Adobe Developer Documentation](https://marketing.adobe.com/developer/)

[The API Explorer](https://marketing.adobe.com/developer/api-explorer)

[DataSources.UploadData](https://marketing.adobe.com/developer/documentation/data-sources/r-uploaddatadatasources)
