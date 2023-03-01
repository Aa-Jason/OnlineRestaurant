package com.shop.takeout.dto;



import com.shop.takeout.entity.Setmeal;
import com.shop.takeout.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
