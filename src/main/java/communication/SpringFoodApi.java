package communication;

import communication.rests.RestRequest;

import java.util.List;


public class SpringFoodApi implements FoodApi {
    private RestRequest restRequest;
    public SpringFoodApi(){
        restRequest = new RestRequest();
    }
    @Override
    public String getFoodById(String id) {
        String url = "http://localhost:12001/food/get?serial="+id;
        String result=restRequest.sendRequest(url);
        if(result!=null)return result;
        else
        return null;
    }

    @Override
    public List<String> getFoods(Integer offset, Integer limit) {
        return null;
    }

    @Override
    public List<String> getAllFood() {
        return null;
    }

    @Override
    public String updateFood(String food) {
        return null;
    }
}
