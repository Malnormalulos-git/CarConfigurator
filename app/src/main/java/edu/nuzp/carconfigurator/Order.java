package edu.nuzp.carconfigurator;

import android.content.ContentValues;

public class Order {

    private int _id;
    private int model_id;
    private int engine_id;
    private int color_id;
    private int configuration_id;

    public Order(int _id, int model_id, int engine_id, int color_id, int configuration_id) {
        this._id = _id;
        this.model_id = model_id;
        this.engine_id = engine_id;
        this.color_id = color_id;
        this.configuration_id = configuration_id;
    }

    public Order() {
        this._id = 0;
        this.model_id = 0;
        this.engine_id = 0;
        this.color_id = 0;
        this.configuration_id = 0;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getModel_id() {
        return model_id;
    }

    public void setModel_id(int model_id) {
        this.model_id = model_id;
    }

    public int getEngine_id() {
        return engine_id;
    }

    public void setEngine_id(int engine_id) {
        this.engine_id = engine_id;
    }

    public int getColor_id() {
        return color_id;
    }

    public void setColor_id(int color_id) {
        this.color_id = color_id;
    }

    public int getConfiguration_id() {
        return configuration_id;
    }

    public void setConfiguration_id(int configuration_id) {
        this.configuration_id = configuration_id;
    }

    public ContentValues getAsContentValues(){
        ContentValues ct = new ContentValues();

        ct.put("model_id", model_id);
        ct.put("engine_id", engine_id);
        ct.put("color_id", color_id);
        ct.put("configuration_id", configuration_id);

        return ct;
    }
    public boolean isCompleted(){
        return model_id != 0 && engine_id != 0 && color_id != 0 && configuration_id != 0;
    }
}
