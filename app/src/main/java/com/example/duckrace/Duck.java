package com.example.duckrace;

class Duck {
    private String name;
    private String icon;
    private int position;
    private int betAmount;

    public Duck(String name, String icon, int position, int betAmount) {
        this.name = name;
        this.icon = icon;
        this.position = position;
        this.betAmount = betAmount;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

    public int getBetAmount() { return betAmount; }
    public void setBetAmount(int betAmount) { this.betAmount = betAmount; }
}


