package gr.uoa.di.madgik.publicfood.Utilities;

import gr.uoa.di.madgik.publicfood.limesurvey.limesurveyDAO.SurveyAnswers;
import gr.uoa.di.madgik.publicfood.model.ChildrenEntity;
import gr.uoa.di.madgik.publicfood.model.enums.BMIClasses;
import gr.uoa.di.madgik.publicfood.model.enums.Gender;
import gr.uoa.di.madgik.publicfood.security.httprequests.ResponseKeys;

import java.util.HashMap;
import java.util.Map;

import static gr.uoa.di.madgik.publicfood.model.enums.Gender.*;

public class BMICalculation {


    public static Map<String, String> calculateBMI(ChildrenEntity child){

        Map<String, String> responseMap = new HashMap<String, String>();

        int weight = Integer.parseInt(child.getWeight());
        int height = child.getHeight();
        double bmi = (100 * 100 * (double) weight) / (double) (height * height);

        System.out.println("Weight: " + weight + " Height: " + height + " BMI: " + bmi);
        BMIClasses result = bmiCategory(bmi, child);

        bmi = SurveyAnswers.round(bmi, 3);
        responseMap.put(ResponseKeys.ChildController_BMI_Value, String.valueOf(bmi));
        responseMap.put(ResponseKeys.ChildController_BMI_Class, String.valueOf(result.getValue()));

        return responseMap;
    }



    private static BMIClasses bmiCategory(double bmi, ChildrenEntity child){

        Gender gender = valueOf(child.getGender());
        BMIClasses result;

        if(gender != null) {

            switch (gender) {
                case Male:
                    result = maleCalculation(child, bmi);
                    return result;
                case Female:
                    result = femaleCalculation(child, bmi);
                    return result;
            }
        }

        return BMIClasses.Undefined;

    }

    private static BMIClasses maleCalculation(ChildrenEntity child, double bmi)
    {
        switch (child.getAge())
        {
            case 6:
                if(bmi <= 13.434){

                    return BMIClasses.Underweight;
                }
                else if(bmi > 13.434 && bmi <=16.944)
                {
                    return BMIClasses.Healthy;
                }
                else if(bmi > 16.944 && bmi <= 18.034){
                    return BMIClasses.Overweight;
                }
                else
                {
                    return BMIClasses.Obese;
                }
            case 7:
                if(bmi <= 13.854){
                    return BMIClasses.Underweight;

                }
                else if(bmi > 13.854 && bmi <=17.284)
                {
                    return BMIClasses.Healthy;

                }
                else if(bmi > 17.284 && bmi <= 18.514){
                    return BMIClasses.Overweight;

                }
                else
                {
                    return BMIClasses.Obese;

                }
            case 8:
                if(bmi <= 13.754){
                    return BMIClasses.Underweight;

                }
                else if(bmi > 13.754 && bmi <=17.724)
                {
                    return BMIClasses.Healthy;

                }
                else if(bmi > 17.724 && bmi <= 19.104){
                    return BMIClasses.Overweight;

                }
                else
                {
                    return BMIClasses.Obese;

                }
            case 9:
                if(bmi <= 13.984){
                    return BMIClasses.Underweight;

                }
                else if(bmi > 13.984 && bmi <=18.254)
                {
                    return BMIClasses.Healthy;

                }
                else if(bmi > 18.254 && bmi <= 19.804){
                    return BMIClasses.Overweight;

                }
                else
                {
                    return BMIClasses.Obese;

                }
            case 10:
                if(bmi <= 14.284){
                    return BMIClasses.Underweight;

                }
                else if(bmi > 14.284 && bmi <=18.884)
                {
                    return BMIClasses.Healthy;

                }
                else if(bmi > 18.884 && bmi <= 20.634){
                    return BMIClasses.Overweight;

                }
                else
                {
                    return BMIClasses.Obese;

                }
            case 11:
                if(bmi <= 14.664){
                    return BMIClasses.Underweight;

                }
                else if(bmi > 14.664 && bmi <=19.614)
                {
                    return BMIClasses.Healthy;

                }
                else if(bmi > 19.614 && bmi <= 21.564){
                    return BMIClasses.Overweight;

                }
                else
                {
                    return BMIClasses.Obese;

                }
        }
        return BMIClasses.Undefined;
    }

    private static BMIClasses femaleCalculation(ChildrenEntity child, double bmi)
    {
        switch (child.getAge())
        {
            case 6:
                if(bmi <= 13.104){
                    return BMIClasses.Underweight;

                }
                else if(bmi > 13.104 && bmi <=17.204)
                {
                    return BMIClasses.Healthy;

                }
                else if(bmi > 17.204 && bmi <= 18.564){
                    return BMIClasses.Overweight;

                }
                else
                {
                    return BMIClasses.Obese;

                }
            case 7:
                if(bmi <= 14.204){
                    return BMIClasses.Underweight;

                }
                else if(bmi > 14.204 && bmi <=17.564)
                {
                    return BMIClasses.Healthy;

                }
                else if(bmi > 17.564 && bmi <= 19.104){
                    return BMIClasses.Overweight;

                }
                else
                {
                    return BMIClasses.Obese;

                }
            case 8:
                if(bmi <= 13.414){
                    return BMIClasses.Underweight;

                }
                else if(bmi > 13.414 && bmi <=18.104)
                {
                    return BMIClasses.Healthy;

                }
                else if(bmi > 18.104 && bmi <= 19.804){
                    return BMIClasses.Overweight;

                }
                else
                {
                    return BMIClasses.Obese;

                }
            case 9:
                if(bmi <= 13.734){
                    return BMIClasses.Underweight;

                }
                else if(bmi > 13.734 && bmi <=18.734)
                {
                    return BMIClasses.Healthy;

                }
                else if(bmi > 18.734 && bmi <= 20.644){
                    return BMIClasses.Overweight;

                }
                else
                {
                    return BMIClasses.Obese;

                }
            case 10:
                if(bmi <= 14.124){
                    return BMIClasses.Underweight;

                }
                else if(bmi > 14.124 && bmi <= 19.514)
                {
                    return BMIClasses.Healthy;

                }
                else if(bmi > 19.514 && bmi <= 20.764){
                    return BMIClasses.Overweight;

                }
                else
                {
                    return BMIClasses.Obese;

                }
            case 11:
                if(bmi <= 14.614){
                    return BMIClasses.Underweight;

                }
                else if(bmi > 14.614 && bmi <= 20.404)
                {
                    return BMIClasses.Healthy;

                }
                else if(bmi > 20.404 && bmi <= 22.664){
                    return BMIClasses.Overweight;

                }
                else
                {
                    return BMIClasses.Obese;

                }
        }
        return BMIClasses.Undefined;
    }
}
