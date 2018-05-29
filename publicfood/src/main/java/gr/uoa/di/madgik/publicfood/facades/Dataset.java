package gr.uoa.di.madgik.publicfood.facades;

import gr.uoa.di.madgik.publicfood.capsella_authentication_service.User;
import gr.uoa.di.madgik.publicfood.limesurvey.utilities.SurveyStrings;
import gr.uoa.di.madgik.publicfood.security.httprequests.ResponseKeys;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Dataset extends Facade {

    private int id;
    private String name;

    private double parentsDontHaveTimeToCookVegetables;
    private double childrenEatBreakfastAtHome;
    private double childrenEatBreakfastAtHomeMale;
    private double childrenEatBreakfastAtHomeFemale;
    private double breakfastFrequency;
    private double breakfastFrequencyNever;
    private double childrenWhichEatSnacksBetweenMeals;
    private double childrenWhichEatFruitProvidedBySchool;
    private double parentsBelieveThatOrganicFoodIsExpensive;
    private double parentsBelieveThatOrganicVegetablesAreExpensive;
    private double smoothiesAreAvailableHome;
    private double fruitsAreAvailableHome;

    private double breakfastPopularIngredients_milk_yogurt;
    private double breakfastPopularIngredients_biscuits_cake;
    private double breakfastPopularIngredients_bread;
    private double breakfastPopularIngredients_snacks;
    private double breakfastPopularIngredients_cereal;
    private double breakfastPopularIngredients_dried_fruits;
    private double breakfastPopularIngredients_tea;
    private double breakfastPopularIngredients_fruit_juice;
    private double breakfastPopularIngredients_coffee;
    private double breakfastPopularIngredients_eggs;

    private double physicalActivities_Athletics;
    private double physicalActivities_Basketball;
    private double physicalActivities_Cycling;
    private double physicalActivities_Dancing;
    private double physicalActivities_Gymnastics;
    private double physicalActivities_Soccer;
    private double physicalActivities_Swimming;
    private double physicalActivities_Tennis;
    private double physicalActivities_Volleyball;
    private double physicalActivities_Other;

    private double exerciseHours_0_1_hours;
    private double exerciseHours_1_3_hours;
    private double exerciseHours_3_7_hours;
    private double exerciseHours_moreThan_7_hours;


    private double favorite_snacks_Fruit_Or_Vegetables;
    private double favorite_snacks_Savoury_Snack_Bar;
    private double favorite_snacks_Sweet_Snack_Bar;
    private double favorite_snacks_Nuts;
    private double favorite_snacks_Chocolate;
    private double favorite_snacks_Candy;
    private double favorite_snacks_Cakes_And_Biscuits;
    private double favorite_snacks_Bread_And_Cured_Meat;
    private double favorite_snacks_Other;

    //  private double doNotHaveTimeToCookVegetables;


    public double getFavorite_snacks_Fruit_Or_Vegetables() {
        return favorite_snacks_Fruit_Or_Vegetables;
    }

    public void setFavorite_snacks_Fruit_Or_Vegetables(double favorite_snacks_Fruit_Or_Vegetables) {
        this.favorite_snacks_Fruit_Or_Vegetables = favorite_snacks_Fruit_Or_Vegetables;
    }

    public double getFavorite_snacks_Savoury_Snack_Bar() {
        return favorite_snacks_Savoury_Snack_Bar;
    }

    public void setFavorite_snacks_Savoury_Snack_Bar(double favorite_snacks_Savoury_Snack_Bar) {
        this.favorite_snacks_Savoury_Snack_Bar = favorite_snacks_Savoury_Snack_Bar;
    }

    public double getFavorite_snacks_Sweet_Snack_Bar() {
        return favorite_snacks_Sweet_Snack_Bar;
    }

    public void setFavorite_snacks_Sweet_Snack_Bar(double favorite_snacks_Sweet_Snack_Bar) {
        this.favorite_snacks_Sweet_Snack_Bar = favorite_snacks_Sweet_Snack_Bar;
    }

    public double getFavorite_snacks_Nuts() {
        return favorite_snacks_Nuts;
    }

    public void setFavorite_snacks_Nuts(double favorite_snacks_Nuts) {
        this.favorite_snacks_Nuts = favorite_snacks_Nuts;
    }

    public double getFavorite_snacks_Chocolate() {
        return favorite_snacks_Chocolate;
    }

    public void setFavorite_snacks_Chocolate(double favorite_snacks_Chocolate) {
        this.favorite_snacks_Chocolate = favorite_snacks_Chocolate;
    }

    public double getFavorite_snacks_Candy() {
        return favorite_snacks_Candy;
    }

    public void setFavorite_snacks_Candy(double favorite_snacks_Candy) {
        this.favorite_snacks_Candy = favorite_snacks_Candy;
    }

    public double getFavorite_snacks_Cakes_And_Biscuits() {
        return favorite_snacks_Cakes_And_Biscuits;
    }

    public void setFavorite_snacks_Cakes_And_Biscuits(double favorite_snacks_Cakes_And_Biscuits) {
        this.favorite_snacks_Cakes_And_Biscuits = favorite_snacks_Cakes_And_Biscuits;
    }

    public double getFavorite_snacks_Bread_And_Cured_Meat() {
        return favorite_snacks_Bread_And_Cured_Meat;
    }

    public void setFavorite_snacks_Bread_And_Cured_Meat(double favorite_snacks_Bread_And_Cured_Meat) {
        this.favorite_snacks_Bread_And_Cured_Meat = favorite_snacks_Bread_And_Cured_Meat;
    }

    public double getFavorite_snacks_Other() {
        return favorite_snacks_Other;
    }

    public void setFavorite_snacks_Other(double favorite_snacks_Other) {
        this.favorite_snacks_Other = favorite_snacks_Other;
    }

    public double getExerciseHours_0_1_hours() {
        return exerciseHours_0_1_hours;
    }

    public void setExerciseHours_0_1_hours(double exerciseHours_0_1_hours) {
        this.exerciseHours_0_1_hours = exerciseHours_0_1_hours;
    }

    public double getExerciseHours_1_3_hours() {
        return exerciseHours_1_3_hours;
    }

    public void setExerciseHours_1_3_hours(double exerciseHours_1_3_hours) {
        this.exerciseHours_1_3_hours = exerciseHours_1_3_hours;
    }

    public double getExerciseHours_3_7_hours() {
        return exerciseHours_3_7_hours;
    }

    public void setExerciseHours_3_7_hours(double exerciseHours_3_7_hours) {
        this.exerciseHours_3_7_hours = exerciseHours_3_7_hours;
    }

    public double getExerciseHours_moreThan_7_hours() {
        return exerciseHours_moreThan_7_hours;
    }

    public void setExerciseHours_moreThan_7_hours(double exerciseHours_moreThan_7_hours) {
        this.exerciseHours_moreThan_7_hours = exerciseHours_moreThan_7_hours;
    }

    public double getPhysicalActivities_Athletics() {
        return physicalActivities_Athletics;
    }

    public void setPhysicalActivities_Athletics(double physicalActivities_Athletics) {
        this.physicalActivities_Athletics = physicalActivities_Athletics;
    }

    public double getPhysicalActivities_Basketball() {
        return physicalActivities_Basketball;
    }

    public void setPhysicalActivities_Basketball(double physicalActivities_Basketball) {
        this.physicalActivities_Basketball = physicalActivities_Basketball;
    }

    public double getPhysicalActivities_Cycling() {
        return physicalActivities_Cycling;
    }

    public void setPhysicalActivities_Cycling(double physicalActivities_Cycling) {
        this.physicalActivities_Cycling = physicalActivities_Cycling;
    }

    public double getPhysicalActivities_Dancing() {
        return physicalActivities_Dancing;
    }

    public void setPhysicalActivities_Dancing(double physicalActivities_Dancing) {
        this.physicalActivities_Dancing = physicalActivities_Dancing;
    }

    public double getPhysicalActivities_Gymnastics() {
        return physicalActivities_Gymnastics;
    }

    public void setPhysicalActivities_Gymnastics(double physicalActivities_Gymnastics) {
        this.physicalActivities_Gymnastics = physicalActivities_Gymnastics;
    }

    public double getPhysicalActivities_Soccer() {
        return physicalActivities_Soccer;
    }

    public void setPhysicalActivities_Soccer(double physicalActivities_Soccer) {
        this.physicalActivities_Soccer = physicalActivities_Soccer;
    }

    public double getPhysicalActivities_Swimming() {
        return physicalActivities_Swimming;
    }

    public void setPhysicalActivities_Swimming(double physicalActivities_Swimming) {
        this.physicalActivities_Swimming = physicalActivities_Swimming;
    }

    public double getPhysicalActivities_Tennis() {
        return physicalActivities_Tennis;
    }

    public void setPhysicalActivities_Tennis(double physicalActivities_Tennis) {
        this.physicalActivities_Tennis = physicalActivities_Tennis;
    }

    public double getPhysicalActivities_Volleyball() {
        return physicalActivities_Volleyball;
    }

    public void setPhysicalActivities_Volleyball(double physicalActivities_Volleyball) {
        this.physicalActivities_Volleyball = physicalActivities_Volleyball;
    }

    public double getPhysicalActivities_Other() {
        return physicalActivities_Other;
    }

    public void setPhysicalActivities_Other(double physicalActivities_Other) {
        this.physicalActivities_Other = physicalActivities_Other;
    }

    public double getBreakfastPopularIngredients_milk_yogurt() {
        return breakfastPopularIngredients_milk_yogurt;
    }

    public void setBreakfastPopularIngredients_milk_yogurt(double breakfastPopularIngredients_milk_yogurt) {
        this.breakfastPopularIngredients_milk_yogurt = breakfastPopularIngredients_milk_yogurt;
    }

    public double getBreakfastPopularIngredients_biscuits_cake() {
        return breakfastPopularIngredients_biscuits_cake;
    }

    public void setBreakfastPopularIngredients_biscuits_cake(double breakfastPopularIngredients_biscuits_cake) {
        this.breakfastPopularIngredients_biscuits_cake = breakfastPopularIngredients_biscuits_cake;
    }

    public double getBreakfastPopularIngredients_bread() {
        return breakfastPopularIngredients_bread;
    }

    public void setBreakfastPopularIngredients_bread(double breakfastPopularIngredients_bread) {
        this.breakfastPopularIngredients_bread = breakfastPopularIngredients_bread;
    }

    public double getBreakfastPopularIngredients_snacks() {
        return breakfastPopularIngredients_snacks;
    }

    public void setBreakfastPopularIngredients_snacks(double breakfastPopularIngredients_snacks) {
        this.breakfastPopularIngredients_snacks = breakfastPopularIngredients_snacks;
    }

    public double getBreakfastPopularIngredients_cereal() {
        return breakfastPopularIngredients_cereal;
    }

    public void setBreakfastPopularIngredients_cereal(double breakfastPopularIngredients_cereal) {
        this.breakfastPopularIngredients_cereal = breakfastPopularIngredients_cereal;
    }

    public double getBreakfastPopularIngredients_dried_fruits() {
        return breakfastPopularIngredients_dried_fruits;
    }

    public void setBreakfastPopularIngredients_dried_fruits(double breakfastPopularIngredients_dried_fruits) {
        this.breakfastPopularIngredients_dried_fruits = breakfastPopularIngredients_dried_fruits;
    }

    public double getBreakfastPopularIngredients_tea() {
        return breakfastPopularIngredients_tea;
    }

    public void setBreakfastPopularIngredients_tea(double breakfastPopularIngredients_tea) {
        this.breakfastPopularIngredients_tea = breakfastPopularIngredients_tea;
    }

    public double getBreakfastPopularIngredients_fruit_juice() {
        return breakfastPopularIngredients_fruit_juice;
    }

    public void setBreakfastPopularIngredients_fruit_juice(double breakfastPopularIngredients_fruit_juice) {
        this.breakfastPopularIngredients_fruit_juice = breakfastPopularIngredients_fruit_juice;
    }

    public double getBreakfastPopularIngredients_coffee() {
        return breakfastPopularIngredients_coffee;
    }

    public void setBreakfastPopularIngredients_coffee(double breakfastPopularIngredients_coffee) {
        this.breakfastPopularIngredients_coffee = breakfastPopularIngredients_coffee;
    }

    public double getBreakfastPopularIngredients_eggs() {
        return breakfastPopularIngredients_eggs;
    }

    public void setBreakfastPopularIngredients_eggs(double breakfastPopularIngredients_eggs) {
        this.breakfastPopularIngredients_eggs = breakfastPopularIngredients_eggs;
    }

    public double getParentsBelieveThatOrganicVegetablesAreExpensive() {
        return parentsBelieveThatOrganicVegetablesAreExpensive;
    }

    public void setParentsBelieveThatOrganicVegetablesAreExpensive(double parentsBelieveThatOrganicVegetablesAreExpensive) {
        this.parentsBelieveThatOrganicVegetablesAreExpensive = parentsBelieveThatOrganicVegetablesAreExpensive;
    }

    public double getFruitsAreAvailableHome() {
        return fruitsAreAvailableHome;
    }

    public void setFruitsAreAvailableHome(double fruitsAreAvailableHome) {
        this.fruitsAreAvailableHome = fruitsAreAvailableHome;
    }

    public double getBreakfastFrequencyNever() {
        return breakfastFrequencyNever;
    }

    public void setBreakfastFrequencyNever(double breakfastFrequencyNever) {
        this.breakfastFrequencyNever = breakfastFrequencyNever;
    }

    public double getChildrenEatBreakfastAtHomeMale() {
        return childrenEatBreakfastAtHomeMale;
    }

    public void setChildrenEatBreakfastAtHomeMale(double childrenEatBreakfastAtHomeMale) {
        this.childrenEatBreakfastAtHomeMale = childrenEatBreakfastAtHomeMale;
    }

    public double getChildrenEatBreakfastAtHomeFemale() {
        return childrenEatBreakfastAtHomeFemale;
    }

    public void setChildrenEatBreakfastAtHomeFemale(double childrenEatBreakfastAtHomeFemale) {
        this.childrenEatBreakfastAtHomeFemale = childrenEatBreakfastAtHomeFemale;
    }

    public double getChildrenWhichEatFruitProvidedBySchool() {
        return childrenWhichEatFruitProvidedBySchool;
    }

    public void setChildrenWhichEatFruitProvidedBySchool(double childrenWhichEatFruitProvidedBySchool) {
        this.childrenWhichEatFruitProvidedBySchool = childrenWhichEatFruitProvidedBySchool;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getParentsBelieveThatOrganicFoodIsExpensive() {
        return parentsBelieveThatOrganicFoodIsExpensive;
    }

    public void setParentsBelieveThatOrganicFoodIsExpensive(double parentsBelieveThatOrganicFoodIsExpensive) {
        this.parentsBelieveThatOrganicFoodIsExpensive = parentsBelieveThatOrganicFoodIsExpensive;
    }

    public double getParentsDontHaveTimeToCookVegetables() {
        return parentsDontHaveTimeToCookVegetables;
    }

    public void setParentsDontHaveTimeToCookVegetables(double parentsDontHaveTimeToCookVegetables) {
        this.parentsDontHaveTimeToCookVegetables = parentsDontHaveTimeToCookVegetables;
    }

    public double getChildrenEatBreakfastAtHome() {
        return childrenEatBreakfastAtHome;
    }

    public void setChildrenEatBreakfastAtHome(double childrenEatBreakfastAtHome) {
        this.childrenEatBreakfastAtHome = childrenEatBreakfastAtHome;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBreakfastFrequency() {
        return breakfastFrequency;
    }

    public void setBreakfastFrequency(double breakfastFrequency) {
        this.breakfastFrequency = breakfastFrequency;
    }

    public double getChildrenWhichEatSnacksBetweenMeals() {
        return childrenWhichEatSnacksBetweenMeals;
    }

    public void setChildrenWhichEatSnacksBetweenMeals(double childrenWhichEatSnacksBetweenMeals) {
        this.childrenWhichEatSnacksBetweenMeals = childrenWhichEatSnacksBetweenMeals;
    }

    public double getSmoothiesAreAvailableHome() {
        return smoothiesAreAvailableHome;
    }

    public void setSmoothiesAreAvailableHome(double smoothiesAreAvailableHome) {
        this.smoothiesAreAvailableHome = smoothiesAreAvailableHome;
    }

    @Override
    public Object Serialize() throws JSONException {
        return null;
    }

    @Override
    public Dataset Deserialize(Object payload) throws JSONException {

        JSONObject jsonObject = (payload instanceof JSONObject) ? (JSONObject) payload : new JSONObject((String) payload);

        Dataset dataset = new Dataset();
        dataset.setId(jsonObject.getInt("id"));
        dataset.setFavorite_snacks_Other(jsonObject.getDouble("favorite_snacks_Other"));
        dataset.setFavorite_snacks_Bread_And_Cured_Meat(jsonObject.getDouble("favorite_snacks_Bread_And_Cured_Meat"));
        dataset.setFavorite_snacks_Cakes_And_Biscuits(jsonObject.getDouble("favorite_snacks_Cakes_And_Biscuits"));
        dataset.setFavorite_snacks_Candy(jsonObject.getDouble("favorite_snacks_Candy"));
        dataset.setFavorite_snacks_Chocolate(jsonObject.getDouble("favorite_snacks_Chocolate"));
        dataset.setFavorite_snacks_Nuts(jsonObject.getDouble("favorite_snacks_Nuts"));
        dataset.setFavorite_snacks_Sweet_Snack_Bar(jsonObject.getDouble("favorite_snacks_Sweet_Snack_Bar"));
        dataset.setFavorite_snacks_Savoury_Snack_Bar(jsonObject.getDouble("favorite_snacks_Savoury_Snack_Bar"));
        dataset.setExerciseHours_moreThan_7_hours(jsonObject.getDouble("exerciseHours_moreThan_7_hours"));
        dataset.setExerciseHours_3_7_hours(jsonObject.getDouble("exerciseHours_3_7_hours"));
        dataset.setExerciseHours_1_3_hours(jsonObject.getDouble("exerciseHours_1_3_hours"));
        dataset.setExerciseHours_0_1_hours(jsonObject.getDouble("exerciseHours_0_1_hours"));
        dataset.setPhysicalActivities_Other(jsonObject.getDouble("physicalActivities_Other"));
        dataset.setPhysicalActivities_Volleyball(jsonObject.getDouble("physicalActivities_Volleyball"));
        dataset.setPhysicalActivities_Tennis(jsonObject.getDouble("physicalActivities_Tennis"));
        dataset.setPhysicalActivities_Swimming(jsonObject.getDouble("physicalActivities_Swimming"));
        dataset.setPhysicalActivities_Soccer(jsonObject.getDouble("physicalActivities_Soccer"));
        dataset.setPhysicalActivities_Gymnastics(jsonObject.getDouble("physicalActivities_Gymnastics"));
        dataset.setPhysicalActivities_Dancing(jsonObject.getDouble("physicalActivities_Dancing"));
        dataset.setPhysicalActivities_Cycling(jsonObject.getDouble("physicalActivities_Cycling"));
        dataset.setPhysicalActivities_Basketball(jsonObject.getDouble("physicalActivities_Basketball"));
        dataset.setPhysicalActivities_Athletics(jsonObject.getDouble("physicalActivities_Athletics"));
        dataset.setBreakfastPopularIngredients_eggs(jsonObject.getDouble("breakfastPopularIngredients_eggs"));
        dataset.setBreakfastPopularIngredients_coffee(jsonObject.getDouble("breakfastPopularIngredients_coffee"));
        dataset.setBreakfastPopularIngredients_fruit_juice(jsonObject.getDouble("breakfastPopularIngredients_fruit_juice"));
        dataset.setBreakfastPopularIngredients_tea(jsonObject.getDouble("breakfastPopularIngredients_tea"));
        dataset.setBreakfastPopularIngredients_dried_fruits(jsonObject.getDouble("breakfastPopularIngredients_dried_fruits"));
        dataset.setBreakfastPopularIngredients_cereal(jsonObject.getDouble("breakfastPopularIngredients_cereal"));
        dataset.setBreakfastPopularIngredients_snacks(jsonObject.getDouble("breakfastPopularIngredients_snacks"));
        dataset.setBreakfastPopularIngredients_bread(jsonObject.getDouble("breakfastPopularIngredients_bread"));
        dataset.setBreakfastPopularIngredients_biscuits_cake(jsonObject.getDouble("breakfastPopularIngredients_biscuits_cake"));
        dataset.setBreakfastPopularIngredients_milk_yogurt(jsonObject.getDouble("breakfastPopularIngredients_milk_yogurt"));
        dataset.setBreakfastFrequencyNever(jsonObject.getDouble("breakfastFrequencyNever"));
        dataset.setBreakfastFrequency(jsonObject.getDouble("breakfastFrequency"));
        dataset.setChildrenEatBreakfastAtHomeFemale(jsonObject.getDouble("childrenEatBreakfastAtHomeFemale"));
        dataset.setChildrenEatBreakfastAtHomeMale(jsonObject.getDouble("childrenEatBreakfastAtHomeMale"));
        dataset.setFruitsAreAvailableHome(jsonObject.getDouble("fruitsAreAvailableHome"));
        dataset.setSmoothiesAreAvailableHome(jsonObject.getDouble("smoothiesAreAvailableHome"));
        dataset.setParentsBelieveThatOrganicFoodIsExpensive(jsonObject.getDouble("parentsBelieveThatOrganicFoodIsExpensive"));
        dataset.setChildrenWhichEatFruitProvidedBySchool(jsonObject.getDouble("childrenWhichEatFruitProvidedBySchool"));
        dataset.setChildrenWhichEatSnacksBetweenMeals(jsonObject.getDouble("childrenWhichEatSnacksBetweenMeals"));
        dataset.setParentsDontHaveTimeToCookVegetables(jsonObject.getDouble("parentsDontHaveTimeToCookVegetables"));
        dataset.setName(jsonObject.getString("name"));
        dataset.setFavorite_snacks_Fruit_Or_Vegetables(jsonObject.getDouble("favorite_snacks_Fruit_Or_Vegetables"));
        dataset.setParentsBelieveThatOrganicVegetablesAreExpensive(jsonObject.getDouble("parentsBelieveThatOrganicVegetablesAreExpensive"));
        dataset.setChildrenEatBreakfastAtHome(jsonObject.getDouble("childrenEatBreakfastAtHome"));

        return dataset;
    }

//    public double getDoNotHaveTimeToCookVegetables() {
//        return doNotHaveTimeToCookVegetables;
//    }
//
//    public void setDoNotHaveTimeToCookVegetables(double doNotHaveTimeToCookVegetables) {
//        this.doNotHaveTimeToCookVegetables = doNotHaveTimeToCookVegetables;
//    }
}
