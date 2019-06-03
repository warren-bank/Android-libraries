package com.github.warren_bank.filterablerecyclerview;

abstract public class Filter extends android.widget.Filter {
    public CharSequence constraint = "";

    public void query(CharSequence constraint) {
        this.constraint = constraint;

        filter(constraint);
    }
}
