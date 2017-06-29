package com.neurospeech.uiatoms;


import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 */

public class DatePickerEditView extends AppCompatEditText{
    public DatePickerEditView(Context context) {
        super(context);
        init();
    }

    public DatePickerEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DatePickerEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }




    public DateTime getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(DateTime selectedDate) {
        this.selectedDate = selectedDate;
        if(selectedDate != null) {
            this.setText(selectedDate.toString(dateFormat));
        }else {
            this.setText("");
        }
    }

    //Current selected date displayed in Edit Text
    private DateTime selectedDate;


    public OnDateSelectedListener getOnDateSelectedListener() {
        return onDateSelectedListener;
    }

    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
    }

    private OnDateSelectedListener onDateSelectedListener;


    public String getDateFormat() {
        return dateFormat.toString();
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = DateTimeFormat.forPattern(dateFormat);
    }

    private DateTimeFormatter dateFormat;

    private void init() {
        setFocusableInTouchMode(false);
        setDateFormat("yyyy/MM/dd");
        setSelectedDate(DateTime.now());


        setOnClickListener(v -> {
            showDatePicker();
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),(view, year, month, dayOfMonth) -> {
            setSelectedDate(new DateTime(year,month,dayOfMonth,0,0,0));
            if(onDateSelectedListener != null) {
                onDateSelectedListener.onDateSelected(selectedDate);
            }
        },selectedDate.getYear(),selectedDate.getMonthOfYear(),selectedDate.getDayOfMonth());

        datePickerDialog.show();
    }


    public interface OnDateSelectedListener{
        void onDateSelected(DateTime v);
    }

}
