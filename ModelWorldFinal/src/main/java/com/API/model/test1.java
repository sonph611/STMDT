package com.API.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class test1 {
	public static void main(String[] args) {
		new test1().aa();
	}
	public void aa() {
		boolean a=true;
		
		new Thread(()->{
			System.out.println(a);
		}).run();
		
	}
}