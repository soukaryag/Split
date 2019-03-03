package com.example.ashvin.receipts;

public class PieHelper {

    private float startDegree;
    private float endDegree;
    private float targetStartDegree;
    private float targetEndDegree;
    private String title;
    private int color;
    private float sweepDegree;
    private float total;
    private float dollars;

    int velocity = 5;


    public PieHelper(float percent){
        this(percent, null, 0, 1);
    }

    public PieHelper(float percent, int color, String title, float total){
        this(percent, title, color, total);
    }


    PieHelper(float percent, String title){
        this(percent, title, 0, 1);
    }


    PieHelper(float percent, String title, int color, float total){
        this.dollars = percent * 100 * total;
        this.total = total;
        this.sweepDegree = percent * 360 / 100;
        this.title = title;
        this.color = color;
    }


    PieHelper(float startDegree, float endDegree, PieHelper targetPie){
        this.startDegree = startDegree;
        this.endDegree = endDegree;
        targetStartDegree = targetPie.getStartDegree();
        targetEndDegree = targetPie.getEndDegree();
        this.sweepDegree = targetPie.getSweep();
        this.title = targetPie.getTitle();
        this.color = targetPie.getColor();
    }

    PieHelper setTarget(PieHelper targetPie){
        this.targetStartDegree = targetPie.getStartDegree();
        this.targetEndDegree = targetPie.getEndDegree();
        this.title = targetPie.getTitle();
        this.color = targetPie.getColor();
        this.sweepDegree = targetPie.getSweep();
        return this;
    }

    void setDegree(float startDegree, float endDegree){
        this.startDegree = startDegree;
        this.endDegree = endDegree;
    }

    boolean isColorSetted(){return color != 0;}

    boolean isAtRest(){
        return (startDegree==targetStartDegree)&&(endDegree==targetEndDegree);
    }

    void update(){
        this.startDegree = updateSelf(startDegree, targetStartDegree, velocity);
        this.endDegree = updateSelf(endDegree, targetEndDegree, velocity);
        this.sweepDegree = endDegree - startDegree;
    }

    String getPercentStr(){
        float percent = sweepDegree / 360 *100;
        return this.title + " " + String.valueOf((int)percent) + "%";
        // return this.title + " $" + (String.valueOf((int)Math.round(total*percent)));
    }

    public int getColor(){ return color; }

    public String getTitle(){
        return title;
    }

    public float getSweep(){
        return sweepDegree;
    }

    public float getStartDegree(){
        return startDegree;
    }

    public float getEndDegree(){
        return endDegree;
    }

    private float updateSelf(float origin, float target, int velocity){
        if (origin < target) {
            origin += velocity;
        } else if (origin > target){
            origin-= velocity;
        }
        if(Math.abs(target-origin)<velocity){
            origin = target;
        }
        return origin;
    }
}