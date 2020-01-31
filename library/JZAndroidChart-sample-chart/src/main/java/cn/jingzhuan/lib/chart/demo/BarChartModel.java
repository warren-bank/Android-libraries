package cn.jingzhuan.lib.chart.demo;

import android.databinding.ViewDataBinding;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.epoxy.DataBindingEpoxyModel;
import com.airbnb.epoxy.EpoxyModelClass;

import cn.jingzhuan.lib.chart.data.BarDataSet;
import cn.jingzhuan.lib.chart.data.BarValue;
import cn.jingzhuan.lib.chart.demo.databinding.LayoutBarChartBinding;

import java.util.ArrayList;

/**
 * Created by Donglua on 17/8/2.
 */
@EpoxyModelClass(layout = R.layout.layout_bar_chart)
public abstract class BarChartModel extends DataBindingEpoxyModel {

    private BarDataSet barDataSet;

    private static BarValue getBarValue(float value) {
        BarValue barValue = new BarValue(value);
        return barValue;
    }

    private static BarValue getBarValue(float value, int colorTop, int colorBottom) {
        BarValue barValue = new BarValue(value);
        barValue.setGradientColors(colorTop, colorBottom);
        return barValue;
    }

    public BarChartModel() {

        final ArrayList<BarValue> barValueList = new ArrayList<BarValue>();

        barValueList.add(getBarValue(11f));
        barValueList.add(getBarValue(10f));
        barValueList.add(getBarValue(11f));
        barValueList.add(getBarValue(13f));
        barValueList.add(getBarValue(11f));
        barValueList.add(getBarValue(12f));
        barValueList.add(getBarValue(12f));
        barValueList.add(getBarValue(13f, Color.WHITE, Color.BLACK));
        barValueList.add(getBarValue(15f, Color.WHITE, Color.BLACK));

        barDataSet = new BarDataSet(barValueList);
        barDataSet.setAutoBarWidth(true);
    }

    @Override protected View buildView(@NonNull ViewGroup parent) {
        View rootView = super.buildView(parent);

        final LayoutBarChartBinding barBinding = (LayoutBarChartBinding) rootView.getTag();

        barBinding.barChart.setDataSet(barDataSet);
        barBinding.barChart.getAxisRight().setLabelTextColor(Color.BLACK);

        return rootView;
    }

    @Override protected void setDataBindingVariables(ViewDataBinding binding) {
        if (binding instanceof LayoutBarChartBinding) {
            final LayoutBarChartBinding barBinding = (LayoutBarChartBinding) binding;

            barBinding.barChart.animateY(500);
        }
    }
}
