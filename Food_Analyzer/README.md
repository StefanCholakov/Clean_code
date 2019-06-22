# Food Analyzer :hamburger:

Nowdays it is very important to know how many calories we consume.
The Food Analyzer is a simple console, client-server application, written in Java, which allows you to track details about different foods and their ingredients.
The server extracts the needed information from a RESTful API, which is documented [here](https://ndb.nal.usda.gov/ndb/doc/apilist/API-FOOD-REPORTV2.md).
While running, the server can handle many clients at the same time and when information about a certain product is searched, it is added in the server's cache, located on the local file system.

## Commands

-	`get-food <food_name>` - shows the following information about all products with the name specified: full name, UPC code, NDB number and manufacturer
-	`get-food-report <food_ndbno>` - shows detailed report about food ingredients, calories, proteins, fats, carbohydrates and fibers by a given NDB number
-   `get-food-by-barcode --upc=<upc_code>|--img=<barcode_image_file>` - shows detailed information about a certain product by it's barcode if it is available in the server's cache. It is mandatory to specify at least one of the parameters - UPC code or full path to a barcode image on the local file system. If both parameters are specified, img parameter is ignored


## Dependencies

* For running  
[ZXing Core](https://mvnrepository.com/artifact/com.google.zxing/core)  
[ZXing Java SE Extensions](https://mvnrepository.com/artifact/com.google.zxing/javase)  
[Gson](https://mvnrepository.com/artifact/com.google.code.gson/gson)  
 
* For testing  
[Mockito Core](https://mvnrepository.com/artifact/org.mockito/mockito-core)  
[Byte Buddy](https://mvnrepository.com/artifact/net.bytebuddy/byte-buddy)  
[Byte Buddy Java Agent](https://mvnrepository.com/artifact/net.bytebuddy/byte-buddy-agent)  
[Objenesis](https://mvnrepository.com/artifact/org.objenesis/objenesis)  