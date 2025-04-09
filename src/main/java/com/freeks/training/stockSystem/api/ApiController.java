package com.freeks.training.stockSystem.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freeks.training.stockSystem.entity.ItemInfoEntity;
import com.freeks.training.stockSystem.mapper.ItemInfoMapper;


//DBのデータをJsonで返す

@RestController
@RequestMapping("/api/items")
public class ApiController {
	
	@Autowired
	private ItemInfoMapper itemInfoMapper;
	
    @GetMapping("/export-json")
    public ResponseEntity<List<ItemInfoEntity>> exportJson() {
        List<ItemInfoEntity> itemList = itemInfoMapper.getFindAll();
        return ResponseEntity.ok(itemList);
    }
}
