public class CupSizeDial {
    private int selectedSize;

    public void rotateDial(int position) {
        this.selectedSize = position;
        if (position==1)
            System.out.println("Cup size set to: small");
        else if(position==2)
            System.out.println("Cup size set to: medium");
        else if (position==3)
            System.out.println("Cup size set to: large");
    }

    public int getSelectedSize() {
        return selectedSize;
    }
}
