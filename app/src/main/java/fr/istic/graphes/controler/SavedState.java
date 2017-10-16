package fr.istic.graphes.controler;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cavronjeremy on 13/10/2017.
 */

public  class SavedState  {

    private final int number1;
    private final int number2;
    private final int number3;

    private SavedState(Parcelable superState, int number1, int number2, int number3) {
        //super(superState);
        this.number1 = number1;
        this.number2 = number2;
        this.number3 = number3;
    }

    private SavedState(Parcel in) {
        //super(in);
        number1 = in.readInt();
        number2 = in.readInt();
        number3 = in.readInt();
    }

    public int getNumber1() {
        return number1;
    }

    public int getNumber2() {
        return number2;
    }

    public int getNumber3() {
        return number3;
    }

    /*@Override
    public void writeToParcel(Parcel destination, int flags) {
        //super.writeToParcel(destination, flags);
        destination.writeInt(number1);
        destination.writeInt(number2);
        destination.writeInt(number3);
    }

    public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {

        public SavedState createFromParcel(Parcel in) {
            return new SavedState(in);
        }

        public SavedState[] newArray(int size) {
            return new SavedState[size];
        }

    };*/


}
