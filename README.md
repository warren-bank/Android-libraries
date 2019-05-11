#### [Android libraries](https://github.com/warren-bank/Android-libraries/tree/warren-bank/FilterableRecyclerView)

#### API

__interfaces:__

* `FilterableListItem`
  * data type of elements in `List<>`
* `FilterableListItemOnClickListener`
  * contains the callback function that serves as an onclick handler for each row item

__abstract class:__

* `FilterableViewHolder` extends `RecyclerView.ViewHolder`
  * the following abstract methods need to be implemented to achieve the desired behavior:
    * `public void onCreate(View view)`
      * where `view` is the inflated layout (spoiler: you pass a reference for the desired layout to the adapter)
      * intended usage:
        * `onCreate` is called from the `ViewHolder` constructor
        * the class you write to extend this abstract class declares private variables for each UI element that needs to be populated with data
        * this is when such variables would be defined via calls to `view.findViewById()`
    * `public void onUpdate(FilterableListItem item)`
      * where `item` is an element in `List<>`
      * intended usage:
        * `onUpdate` is called from `onBindViewHolder` in the `RecyclerView.Adapter`
        * this is when `view` is being bound to the data that it should represent visually
        * this is when the private variables for UI elements should be assigned values
          * ex: `text1.setText(item.name);`

__final class:__


* `FilterableAdapter` extends `RecyclerView.Adapter`
  * OK.. I lied a little
    * this class isn't marked `final`..<br>but I can't think of any reason you'd want to extend it
  * all you need to do, is pass the following parameters to its constructor:
    * _required:_
      * `int row_layout_id`
        * layout that gets inflated for each row
        * reminder: this inflated `View` gets passed to `onCreate`
      * `List<FilterableListItem> unfilteredList`
        * the complete `List<>` of all data
        * each element implements the `FilterableListItem` interface
      * `FilterableListItemOnClickListener listener`
        * an object that implements the `FilterableListItemOnClickListener` interface
      * `Class filterableViewHolderClass`
        * this is probably best explained by an example:
          ```java
            class SampleFilterableViewHolder extends FilterableViewHolder {
                // definitions: constructor, onCreate, onUpdate
            }
            Class filterableViewHolderClass = SampleFilterableViewHolder.class;
          ```
        * a bit of reflection is used in order to call your `Class` constructor, rather than the abstract base abstract class
    * _optional:_
      * `Class parentClass`
      * `Object parentInstance`
        * these both pertain to `filterableViewHolderClass`, and are also best explained by an example:
          ```java
            public class ParentClass_Activity {
                public class ChildClass_FilterableViewHolder extends FilterableViewHolder {
                    // definitions: constructor, onCreate, onUpdate
                }
            }
            Class filterableViewHolderClass = ChildClass_FilterableViewHolder.class;
            Class parentClass               = ParentClass_Activity.class;
            Object parentInstance           = ParentClass_Activity.this;
          ```
        * the explanation for needing these additional parameters when `filterableViewHolderClass` is an inner/nested class is a bit obscure
          * the source code syntax for the inner class constructor hides the fact that the compiled `Constructor` includes an additional parameter
          * the first parameter to the `Constructor` for an inner class is an instance of the parent `Class`
          * for this reason, in the previous example:
            * `ChildClass_FilterableViewHolder` is an inner class
            * `ParentClass_Activity` is its parent class
              * this `Class` is needed to find the `Constructor` for `ChildClass_FilterableViewHolder` by reflection
              * an instance is needed to be passed to this `Constructor` when it is invoked
  * once you have an instance of `FilterableAdapter`:
    * configure the `RecyclerView` to use it
      * ex: `recyclerView.setAdapter(recyclerFilterableAdapter);`
    * obtain the search `Filter`:
      * ex: `Filter searchFilter = recyclerFilterableAdapter.getFilter();`
    * use the `Filter` to search `List<>` data:
      * ex: `searchFilter.filter("targeted substring");`
    * pass an empty search query to display all results:
      * ex: `searchFilter.filter("");`

#### Sample App:

* [this one file](https://github.com/warren-bank/Android-libraries/blob/warren-bank/FilterableRecyclerView/library/FilterableRecyclerView-sample/src/main/java/com/github/warren_bank/filterablerecyclerview/sample/MainActivity.java) fully demonstrates usage of the library API
* [release build of APK](https://github.com/warren-bank/Android-libraries/releases/download/warren-bank%2FFilterableRecyclerView%2Fv01.00.00/FilterableRecyclerView-sample-release.apk)

#### Notes

* this library was inspired by [the article](https://www.androidhive.info/2017/11/android-recyclerview-with-search-filter-functionality/)

#### Legal:

* copyright: [Warren Bank](https://github.com/warren-bank)
* license: [GPL-2.0](https://www.gnu.org/licenses/old-licenses/gpl-2.0.txt)
