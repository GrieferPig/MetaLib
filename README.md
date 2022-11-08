# Yeetos's Metadata Library

Receives data in proprietary format from http://meta.yeetos-metadata.ml:4616/, welcome to DDOS, not my server anyway(

### used for Yeetos's internal apps only

# usage

```java
new Thread() {
            public void run() {
                try {
                    Log.d("Metadata", readMetadata().rom.latest.version);
                    Log.d("AppData", Objects.requireNonNull(readAppData(MainActivity.this, new Struct())).wow);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

class Struct {
    String wow;
}
```

```xml
    <uses-permission android:name="android.permission.INTERNET" />
```

Yeet.
