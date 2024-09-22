public class WaterInfo {
    protected boolean checkWaterLevelInfo(int waterLevel, int coffeeCup) {
//        if (coffeeCup == CoffeeCupSize.SMALL && waterLevel < 240) return false;
//
//        else if (coffeeCup == CoffeeCupSize.MEDIUM && waterLevel < 340) return false;
//
//        else return coffeeCup != CoffeeCupSize.LARGE || waterLevel >= 440;

        switch (coffeeCup) {

            case 1 -> {
                if (waterLevel < 240) return false;
            }

            case 2 -> {
                if(waterLevel < 360) return false;
            }

            case 3 -> {
                if(waterLevel < 420) return false;
            }
        }
        return true;
    }
}
