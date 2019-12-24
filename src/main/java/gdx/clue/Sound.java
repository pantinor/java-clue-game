package gdx.clue;



public enum Sound {
    
    BUTTON("button.click.wav", false, 0.3f),
    CLICK("click.wav", false, 0.3f),
    CREAK("creaking-door-2.wav", false, 0.3f),
    GIGGLE("Giggle.wav", false, 0.3f),
    DICE("dice.wav", false, 0.3f),
    LAUGH("laugha.wav", false, 0.3f),
    GASP("gasps8.wav", false, 0.3f);
   
    String file;
    boolean looping;
    float volume;

    private Sound(String name, boolean looping, float volume) {
        this.file = name;
        this.looping = looping;
        this.volume = volume;
    }

    public String getFile() {
        return this.file;
    }

    public boolean getLooping() {
        return this.looping;
    }

    public float getVolume() {
        return this.volume;
    }

}
