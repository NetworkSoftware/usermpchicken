package pro.network.jsbroilers.chip;

public class ChipBean {
    String chip;
    String  image;

    public ChipBean(String chip) {
        this.chip = chip;
    }

    public String getChip() {
        return chip;
    }

    public void setChip(String title) {
        this.chip = title;
    }

    public ChipBean(String chip,String image) {
        this.chip = chip;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
