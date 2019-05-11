package com.github.warren_bank.filterablerecyclerview.sample;

import com.github.warren_bank.filterablerecyclerview.FilterableListItem;

import java.util.ArrayList;
import java.util.List;

public class Data {
    public static List<FilterableListItem> getSampleList() {
        List<FilterableListItem> list = new ArrayList<FilterableListItem>();

        list.add(new SampleFilterableListItem("Adam", "Anderson"));
        list.add(new SampleFilterableListItem("Barry", "Benson"));
        list.add(new SampleFilterableListItem("Cathy", "Columbus"));
        list.add(new SampleFilterableListItem("Dennis", "Downy"));
        list.add(new SampleFilterableListItem("Ellen", "Einstein"));
        list.add(new SampleFilterableListItem("Frank", "Frankenstein"));
        list.add(new SampleFilterableListItem("Gary", "Goldwater"));
        list.add(new SampleFilterableListItem("Henry", "Henderson"));
        list.add(new SampleFilterableListItem("Ian", "Islander"));
        list.add(new SampleFilterableListItem("Janet", "Jones"));
        list.add(new SampleFilterableListItem("Kathleen", "Kerry"));
        list.add(new SampleFilterableListItem("Larry", "Landers"));
        list.add(new SampleFilterableListItem("Melody", "Music"));
        list.add(new SampleFilterableListItem("Nathan", "Nelson"));
        list.add(new SampleFilterableListItem("Owen", "Openheimer"));
        list.add(new SampleFilterableListItem("Paul", "Perry"));
        list.add(new SampleFilterableListItem("Quin", "Queensbury"));
        list.add(new SampleFilterableListItem("Ralph", "Richards"));
        list.add(new SampleFilterableListItem("Steven", "Springsteen"));
        list.add(new SampleFilterableListItem("Timothy", "Tillerson"));
        list.add(new SampleFilterableListItem("Ulma", "Underoos"));
        list.add(new SampleFilterableListItem("Vera", "Vanderbeek"));
        list.add(new SampleFilterableListItem("William", "Wandersalot"));
        list.add(new SampleFilterableListItem("Xavier", "Xrays"));
        list.add(new SampleFilterableListItem("Yoko", "Yellowstone"));
        list.add(new SampleFilterableListItem("Zander", "Zoolander"));

        return list;
    }
}
