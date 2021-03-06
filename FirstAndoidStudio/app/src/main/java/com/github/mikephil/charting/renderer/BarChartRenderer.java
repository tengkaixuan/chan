
package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.BarDataProvider;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.util.ArrayList;

public class BarChartRenderer extends DataRenderer {

    protected BarDataProvider mChart;

    /** the rect object that is used for drawing the bar shadow */
    protected RectF mBarShadow = new RectF();

    /** the rect object that is used for drawing the bars */
    protected RectF mBarRect = new RectF();

    public BarChartRenderer(BarDataProvider chart, ChartAnimator animator,
            ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        this.mChart = chart;

        mHighlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighlightPaint.setStyle(Paint.Style.FILL);
        mHighlightPaint.setColor(Color.rgb(0, 0, 0));
        // set alpha after color
        mHighlightPaint.setAlpha(120);
    }
    
    @Override
    public void initBuffers() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void drawData(Canvas c) {

        BarData barData = mChart.getBarData();

        for (int i = 0; i < barData.getDataSetCount(); i++) {

            BarDataSet set = barData.getDataSetByIndex(i);
            if (set.isVisible()) {
                drawDataSet(c, set, i);
            }
        }
    }

    protected void drawDataSet(Canvas c, BarDataSet dataSet, int index) {

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        // the space between bar-groups
        float space = mChart.getBarData().getGroupSpace();

        boolean noStacks = dataSet.getStackSize() == 1 ? true : false;

        ArrayList<BarEntry> entries = dataSet.getYVals();

        // do the drawing
        for (int j = 0; j < dataSet.getEntryCount() * mAnimator.getPhaseX(); j++) {

            BarEntry e = entries.get(j);

            // calculate the x-position, depending on datasetcount
            float x = e.getXIndex() + j * (mChart.getBarData().getDataSetCount() - 1) + index
                    + space * j + space / 2f;
            float y = e.getVal();

            // no stacks
            if (noStacks) {

                prepareBar(x, y, dataSet.getBarSpace(), trans);

                // avoid drawing outofbounds values
                if (!mViewPortHandler.isInBoundsRight(mBarRect.left))
                    break;

                if (!mViewPortHandler.isInBoundsLeft(mBarRect.right))
                    continue;

                // if drawing the bar shadow is enabled
                if (mChart.isDrawBarShadowEnabled()) {
                    mRenderPaint.setColor(dataSet.getBarShadowColor());
                    c.drawRect(mBarShadow, mRenderPaint);
                }

                // Set the color for the currently drawn value. If the index
                // is
                // out of bounds, reuse colors.
                mRenderPaint.setColor(dataSet.getColor(j));
                c.drawRect(mBarRect, mRenderPaint);

            } else { // stacked bars

                float[] vals = e.getVals();

                // we still draw stacked bars, but there could be one
                // non-stacked
                // in between
                if (vals == null) {

                    prepareBar(x, y, dataSet.getBarSpace(), trans);

                    // if drawing the bar shadow is enabled
                    if (mChart.isDrawBarShadowEnabled()) {
                        mRenderPaint.setColor(dataSet.getBarShadowColor());
                        c.drawRect(mBarShadow, mRenderPaint);
                    }

                    mRenderPaint.setColor(dataSet.getColor(0));
                    c.drawRect(mBarRect, mRenderPaint);

                } else {

                    float all = e.getVal();

                    // if drawing the bar shadow is enabled
                    if (mChart.isDrawBarShadowEnabled()) {

                        prepareBar(x, y, dataSet.getBarSpace(), trans);
                        mRenderPaint.setColor(dataSet.getBarShadowColor());
                        c.drawRect(mBarShadow, mRenderPaint);
                    }

                    // draw the stack
                    for (int k = 0; k < vals.length; k++) {

                        all -= vals[k];

                        prepareBar(x, vals[k] + all, dataSet.getBarSpace(), trans);

                        mRenderPaint.setColor(dataSet.getColor(k));
                        c.drawRect(mBarRect, mRenderPaint);
                    }
                }

                // avoid drawing outofbounds values
                if (!mViewPortHandler.isInBoundsRight(mBarRect.left))
                    break;
            }
        }
    }

    /**
     * Prepares a bar for drawing on the specified x-index and y-position. Also
     * prepares the shadow-bar if enabled.
     * 
     * @param x the x-position
     * @param y the y-position
     * @param barspace the space between bars
     */
    protected void prepareBar(float x, float y, float barspace, Transformer trans) {

        float barWidth = 0.5f;

        float spaceHalf = barspace / 2f;
        float left = x - barWidth + spaceHalf;
        float right = x + barWidth - spaceHalf;
        float top = y >= 0 ? y : 0;
        float bottom = y <= 0 ? y : 0;

        mBarRect.set(left, top, right, bottom);

        trans.rectValueToPixel(mBarRect, mAnimator.getPhaseY());

        // if a shadow is drawn, prepare it too
        if (mChart.isDrawBarShadowEnabled()) {
            mBarShadow.set(mBarRect.left, mViewPortHandler.offsetTop(), mBarRect.right,
                    mViewPortHandler.contentBottom());
        }
    }

    /**
     * Prepares a bar for being highlighted.
     * 
     * @param x the x-position
     * @param y the y-position
     * @param barspace the space between bars
     * @param from
     * @param trans
     */
    protected void prepareBarHighlight(float x, float y, float barspace, float from, Transformer trans) {

        float barWidth = 0.5f;

        float spaceHalf = barspace / 2f;
        float left = x - barWidth + spaceHalf;
        float right = x + barWidth - spaceHalf;
        float top = y >= from ? y : from;
        float bottom = y <= from ? y : from;

        mBarRect.set(left, top, right, bottom);

        trans.rectValueToPixel(mBarRect, mAnimator.getPhaseY());
    }

    @Override
    public void drawValues(Canvas c) {
        // if values are drawn
        if (passesCheck()) {

            ArrayList<BarDataSet> dataSets = mChart.getBarData().getDataSets();

            float posOffset = 0f;
            float negOffset = 0f;
            boolean drawValueAboveBar = mChart.isDrawValueAboveBarEnabled();

            // calculate the correct offset depending on the draw position of
            // the value
            posOffset = (drawValueAboveBar ? -Utils.convertDpToPixel(5) : Utils.calcTextHeight(
                    mValuePaint,
                    "8") * 1.5f);
            negOffset = (drawValueAboveBar ? Utils.calcTextHeight(mValuePaint, "8") * 1.5f : -Utils
                    .convertDpToPixel(5));


            ArrayList<String> xvals = mChart.getBarData().getXVals();
//            ArrayList<String> newXVals = new ArrayList<String>();
//            int itest = 0;
//            int ipos = 0;
//            for(; itest < xvals.size(); itest++) {
//                String xVal = xvals.get(itest);
//                Log.i("harvey", "newharveyxVal=" + xVal);
//                newXVals.add(ipos, xVal);
//                ipos++;
//                newXVals.add(ipos, xVal);
//                ipos++;
//            }
//
//            for(int temp = 0; temp < newXVals.size(); temp++) {
//                Log.e("harvey", "newXVals="+newXVals.get(temp));
//            }

//            itest = 0;

//            Log.e("harvey", "xvalcount, datasetcount="+mChart.getBarData().getXValCount()+", "+mChart.getBarData().getDataSetCount());
//mChart.getBarData().getXVals();

            for (int i = 0; i < mChart.getBarData().getDataSetCount(); i++) {//onley run one time.

                BarDataSet dataSet = dataSets.get(i);

                if (!dataSet.isDrawValuesEnabled())
                    continue;

//                Log.e("harvey", "i======"+i);
                // apply the text-styling defined by the DataSet
                applyValueTextStyle(dataSet);

                ValueFormatter formatter = dataSet.getValueFormatter();

                Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

                ArrayList<BarEntry> entries = dataSet.getYVals();

                float[] valuePoints = getTransformedValues(trans, entries, i);

                // if only single values are drawn (sum)
                if (!mChart.isDrawValuesForWholeStackEnabled()) {

                    for (int j = 0; j < valuePoints.length * mAnimator.getPhaseX(); j += 2) {

                        if (!mViewPortHandler.isInBoundsRight(valuePoints[j]))
                            break;

                        if (!mViewPortHandler.isInBoundsY(valuePoints[j + 1])
                                || !mViewPortHandler.isInBoundsLeft(valuePoints[j]))
                            continue;

                        float val = entries.get(j / 2).getVal();
                        drawValue(c, val, valuePoints[j],
                                valuePoints[j + 1] + (val >= 0 ? posOffset : negOffset), formatter, "thenullvalue");
                    }

                    // if each value of a potential stack should be drawn
                } else {

//                    Log.e("harvey", "valePoints.length="+valuePoints.length+", getPhaseX="+mAnimator.getPhaseX());
//                    Log.e("harvey", "valuePoints="+valuePoints);
                    int runTime = 0;
                    for (int j = 0; j < (valuePoints.length - 1) * mAnimator.getPhaseX(); j += 2) {

                        BarEntry e = entries.get(j / 2);

                        float[] vals = e.getVals();

                        // we still draw stacked bars, but there is one
                        // non-stacked
                        // in between
                        if (vals == null) {

                            if (!mViewPortHandler.isInBoundsRight(valuePoints[j]))
                                break;

                            if (!mViewPortHandler.isInBoundsY(valuePoints[j + 1])
                                    || !mViewPortHandler.isInBoundsLeft(valuePoints[j]))
                                continue;

                            drawValue(c, e.getVal(), valuePoints[j],
                                    valuePoints[j + 1] + (e.getVal() >= 0 ? posOffset : negOffset),
                                    formatter, xvals.get(j/2));
//                            Log.e("harvey", "xvals.get()="+xvals);

                        } else {

                            float[] transformed = new float[vals.length * 2];
                            int cnt = 0;
                            float add = e.getVal();

                            for (int k = 0; k < transformed.length; k += 2) {

                                add -= vals[cnt];
                                transformed[k + 1] = (vals[cnt] + add) * mAnimator.getPhaseY();
                                cnt++;
                            }

                            trans.pointValuesToPixel(transformed);

                            for (int k = 0; k < transformed.length; k += 2) {

                                float x = valuePoints[j];
                                float y = transformed[k + 1]
                                        + (vals[k / 2] >= 0 ? posOffset : negOffset);

                                if (!mViewPortHandler.isInBoundsRight(x))
                                    break;

                                if (!mViewPortHandler.isInBoundsY(y)
                                        || !mViewPortHandler.isInBoundsLeft(x))
                                    continue;

                                drawValue(c, vals[k / 2], x, y, formatter, "thenullvalue333");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Draws a value at the specified x and y position.
     *
     * @param val
     * @param xPos
     * @param yPos
     * @formatter
     */
    protected void drawValue(Canvas c, float val, float xPos, float yPos, ValueFormatter formatter, String appname) {

        String value = formatter.getFormattedValue(val);
        c.drawText(value, xPos, yPos, mValuePaint);
        int color = mValuePaint.getColor();
        float size = mValuePaint.getTextSize();


        //harvey added 3/10/2015
//        mChart.getBarData().getXvals();

//        Log.i("harvey", "appname========="+appname+", value"+value);
//        Log.d("harvey", "x,y="+xPos+","+yPos+", mBarRect=" + mBarRect);
        c.save();
        String name = appname;//"应用名称写在这";
        Rect bounds = new Rect();
        mValuePaint.getTextBounds(name, 0, name.length(), bounds);
        mValuePaint.setColor(Color.WHITE);
        mValuePaint.setTextSize(18f);
        int text_width = bounds.width();
        float half_text_with = text_width /1.5f;
//        c.rotate(-90f, xPos, mBarRect.bottom-text_width);
        c.rotate(-90f, xPos, yPos+half_text_with);
//        c.drawText("activity name", xPos, mBarRect.bottom-text_width, mValuePaint);
        c.drawText(name, xPos, yPos+half_text_with, mValuePaint);
        c.restore();

        mValuePaint.setColor(color);
        mValuePaint.setTextSize(size);
        //harvey add end 3/10/2015

    }

    @Override
    public void drawExtras(Canvas c) {
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        int setCount = mChart.getBarData().getDataSetCount();

        for (int i = 0; i < indices.length; i++) {

            Highlight h = indices[i];
            int index = h.getXIndex();

            int dataSetIndex = h.getDataSetIndex();
            BarDataSet set = mChart.getBarData().getDataSetByIndex(dataSetIndex);

            if (set == null)
                continue;

            Transformer trans = mChart.getTransformer(set.getAxisDependency());

            mHighlightPaint.setColor(set.getHighLightColor());
            mHighlightPaint.setAlpha(set.getHighLightAlpha());

            // check outofbounds
            if (index < mChart.getBarData().getYValCount() && index >= 0
                    && index < (mChart.getXChartMax() * mAnimator.getPhaseX()) / setCount) {

                BarEntry e = mChart.getBarData().getDataSetByIndex(dataSetIndex)
                        .getEntryForXIndex(index);

                if (e == null)
                    continue;

                float groupspace = mChart.getBarData().getGroupSpace();
                boolean isStack = h.getStackIndex() < 0 ? false : true;

                // calculate the correct x-position
                float x = index * setCount + dataSetIndex + groupspace / 2f
                        + groupspace * index;
                float y = isStack ? e.getVals()[h.getStackIndex()]
                        + e.getBelowSum(h.getStackIndex()) : e.getVal();
                
                // this is where the bar starts
                float from = isStack ? e.getBelowSum(h.getStackIndex()) : 0f;

                prepareBarHighlight(x, y, set.getBarSpace(), from, trans);

                c.drawRect(mBarRect, mHighlightPaint);

                if (mChart.isDrawHighlightArrowEnabled()) {

                    mHighlightPaint.setAlpha(255);

                    // distance between highlight arrow and bar
                    float offsetY = mAnimator.getPhaseY() * 0.07f;

                    Path arrow = new Path();
                    arrow.moveTo(x + 0.5f, y + offsetY * 0.3f);
                    arrow.lineTo(x + 0.2f, y + offsetY);
                    arrow.lineTo(x + 0.8f, y + offsetY);

                    trans.pathValueToPixel(arrow);
                    c.drawPath(arrow, mHighlightPaint);
                }
            }
        }
    }

    public float[] getTransformedValues(Transformer trans, ArrayList<BarEntry> entries,
            int dataSetIndex) {
        return trans.generateTransformedValuesBarChart(entries, dataSetIndex,
                mChart.getBarData(),
                mAnimator.getPhaseY());
    }

    protected boolean passesCheck() {
        return mChart.getBarData().getYValCount() < mChart.getMaxVisibleCount()
                * mViewPortHandler.getScaleX();
    }
}
