package com.lkdz.rfwirelessmoduletest1;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by 廖慧 on 2016/5/23.
 */
public class EditTextUtil implements TextWatcher{


    private int beforeTextLength = 0; //输入前的长度
    private int onTextLength = 0; //文字的长度
    private boolean isChanged = false;
    private int location = 0;//记录光标的位置
    private char[] tempChar;
    private StringBuffer buffer = new StringBuffer();
    private int blankNumber = 0;
    private EditText editText;
    public EditTextUtil(EditText text){
        editText = text;
    }

    @Override
    public void afterTextChanged(Editable arg0) {
        // TODO Auto-generated method stub
        if(isChanged){
            location = editText.getSelectionEnd();
            int index = 0;
            while (index < buffer.length()) {
                if (buffer.charAt(index) == ' ') {
                    buffer.deleteCharAt(index);
                } else {
                    index++;
                }
            }

            index = 0;
            int konggeNumberC = 0;
            while (index < buffer.length()) {
                if ((index == 2 || index == 5 || index == 8 || index == 11 || index==14 || index==17 || index==20)) {
                    buffer.insert(index, ' ');
                    konggeNumberC++;
                }
                index++;
            }

            if (konggeNumberC > blankNumber) {
                location += (konggeNumberC - blankNumber);
            }

            tempChar = new char[buffer.length()];
            buffer.getChars(0, buffer.length(), tempChar, 0);
            String str = buffer.toString();
            if (location > str.length()) {
                location = str.length();
            } else if (location < 0) {
                location = 0;
            }

            editText.setText(str);
            Editable etable = editText.getText();
            Selection.setSelection(etable, location);
            isChanged = false;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence text, int arg1, int arg2,
                                  int arg3) {
        // TODO Auto-generated method stub
        beforeTextLength = text.length();
        if(buffer.length() > 0){
            buffer.delete(0, buffer.length());
        }

        blankNumber = 0;
        for(int i = 0;i < text.length();i ++){
            if(text.charAt(i) == ' '){
                blankNumber ++;
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence text, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        onTextLength = text.length();
        buffer.append(text.toString());
        if(onTextLength == beforeTextLength || onTextLength <= 3
                || isChanged){
            isChanged = false;
            return;
        }

        isChanged = true;
    }

}
