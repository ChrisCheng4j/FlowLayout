# FlowLayout
A flowlayout on android.
## Usage
**Add the dependencies to your gradle file:**
```javascript
dependencies {
	compile 'com.chrischeng:flowlayout:1.1.1'
}
```

The usage is similar to listview and FlowAdapter is the bridge between a FlowLayout and the data that backs the list.  

**XML:**
```xml
<com.chrischeng.flowlayout.FlowLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:fl_spacing_horizontal="15dp" //the spacing between view in one row.
        app:fl_spcaing_vertical="30dp" /> //the spacing between view in one row.
```
**Java:**
And you can also control the spacing in java code.
```java
flowLayout.setHorizontalSpacing(50); //default is 12dp
flowLayout.setVerticalSpacing(50); //default is 12dp
```
Adapter:
```java
@Override
public View getView(int position, ViewGroup parent) {
    TextView textView = new TextView(parent.getContext()); //inflate whatever you want just like android.widget.Adapter
    textview.setText(mDatas.get(position));
    return textView;
}
```
You can receive a callback when a child view's state has bean changed, and you can also set limit of max selected, just like this
```java
flowLayout.setMaxSelectedNum(5);
flowLayout.setOnStateChangedListener(new OnStateChangedListener() {
    @Override
    public void onStateChanged(int pos, boolean isSelected) {
    	...      
    }

    @Override
    public void onMaxNumSelected() {
	...             
    }
});
``` 
