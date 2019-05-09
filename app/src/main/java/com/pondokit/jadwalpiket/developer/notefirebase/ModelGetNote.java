package com.pondokit.jadwalpiket.developer.notefirebase;

public class ModelGetNote {

    private String judul_note;
    private String isi_note;
    private String date_note;

    public ModelGetNote(){}

    public ModelGetNote(String judul_note, String isi_note) {
        this.judul_note = judul_note;
        this.isi_note = isi_note;
        this.date_note = date_note;
    }

    public String getJudul_note() {
        return judul_note;
    }

    public String getDate_note() {
        return date_note;
    }

    public void setDate_note(String date_note) {
        this.date_note = date_note;
    }

    public void setJudul_note(String judul_note) {
        this.judul_note = judul_note;
    }

    public String getIsi_note() {
        return isi_note;
    }

    public void setIsi_note(String isi_note) {
        this.isi_note = isi_note;
    }
}
