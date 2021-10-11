package agh.edu.pl.diet.services.impl;

public enum Type {
    REGULAR(0.5, 2.2, 0.3),
    BODYCUILDING(1.5, 1.0, 0.5),
    KETOGENIC(1.0, 0.5, 1.5);

    private final double multiplicationCalculateProtein;
    private final double multiplicationCalculateCarbohydrates;
    private final double multiplicationCalculateFats;

    public double getTargetWeight() {
        return targetWeight;
    }

    double targetWeight = 0.0;

    Type(double multiplicationCalculateProtein, double multiplicationCalculateCarbohydrates, double multiplicationCalculateFats) {
        this.multiplicationCalculateProtein = multiplicationCalculateProtein;
        this.multiplicationCalculateCarbohydrates = multiplicationCalculateCarbohydrates;
        this.multiplicationCalculateFats = multiplicationCalculateFats;
    }

    public double getMultiplicationCalculateProtein() {
        return multiplicationCalculateProtein;
    }

    public double getMultiplicationCalculateCarbohydrates() {
        return multiplicationCalculateCarbohydrates;
    }

    public double getMultiplicationCalculateFats() {
        return multiplicationCalculateFats;
    }

    public double calculateCalories() {
        double calculatedCalories = calculateProtein() * 4 + calculateCarbohydrates() * 4 + calculateFats() * 9;
        return calculatedCalories;
    }

    public double calculateProtein() {
        double calculatedProtein = targetWeight * multiplicationCalculateProtein;
        return calculatedProtein;
    }

    public double calculateCarbohydrates() {
        double calculatedCarbohydrates = targetWeight * multiplicationCalculateCarbohydrates;
        return calculatedCarbohydrates;
    }

    public double calculateFats() {
        double calculatedFats = targetWeight * multiplicationCalculateFats;
        return calculatedFats;
    }

//    @Component
//    public static class DietTypeImpl extends DietTypeRequest implements DietTypeService {
//
//        @Autowired
//        DietTypeRepo dietTypeRepo;
//
//        @Autowired
//        @Qualifier
//        private Type type;
//
//
//        @Override
//        public List<DietType> getAllDietType() {
//            List<DietType> list = new ArrayList<>();
//            dietTypeRepo.findAll().forEach(list::add);
//            return list;
//        }
//
//        public double calculateCalories() {
//            double calculatedCalories = calculateProtein() * 4 + calculateCarbohydrates() * 4 + calculateFats() * 9;
//            return calculatedCalories;
//        }
//
//        @Override
//        public double calculateProtein() {
//            double calculatedProtein = targetWeight * Double.parseDouble(String.valueOf(multiplicationCalculateProtein));
//            return calculatedProtein;
//        }
//
//        @Override
//        public double calculateCarbohydrates() {
//            double calculatedCarbohydrates = targetWeight * Double.parseDouble(String.valueOf(multiplicationCalculateCarbohydrates));
//            return calculatedCarbohydrates;
//        }
//
//        @Override
//        public double calculateFats() {
//            double calculatedFats = targetWeight * Double.parseDouble(String.valueOf(multiplicationCalculateFats));
//            return calculatedFats;
//        }
//
//        private final Type multiplicationCalculateProtein;
//        private final Type multiplicationCalculateCarbohydrates;
//        private final Type multiplicationCalculateFats;
//
//        public DietTypeImpl(Type multiplicationCalculateProtein, Type multiplicationCalculateCarbohydrates, Type multiplicationCalculateFats) {
//            this.multiplicationCalculateProtein = multiplicationCalculateProtein;
//            this.multiplicationCalculateCarbohydrates = multiplicationCalculateCarbohydrates;
//            this.multiplicationCalculateFats = multiplicationCalculateFats;
//        }
//    }
}