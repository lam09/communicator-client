package communication;

import java.util.List;

public interface FoodApi {
  String getFoodById(String id);
  List<String> getFoods(Integer offset, Integer limit);
  List<String> getAllFood();
  String updateFood(String food);
}
