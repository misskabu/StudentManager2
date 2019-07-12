package com.kenta.tabuchi;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ViewController {
	@Autowired
	StudentRepository repository;
	
	//|---------------------------------------------------------------------------------------
	//|  GET method
	//|---------------------------------------------------------------------------------------
		
	//@RequestMapping(value= {"/","/{order}"},method=RequestMethod.GET)
	//@RequestMapping(value= {"/","/{order}"},method=RequestMethod.GET)
	//public ModelAndView indexGet(@RequestParam(name="order",required=false)Integer order,ModelAndView mav) {
	//mav.setViewName("index");
		
	
	@GetMapping(value= {"/","/{select_id}"})
	public ModelAndView index_get
	(@RequestParam(name="select_id",required=false)Integer select_id,ModelAndView view) 
	{
		String key = null;
		switch(select_id){
			case 0: key = "id";   break;
			case 1: key = "namePhonetic"; break;
			case 2: key = "phone";break;
		}

		view.addObject("recordSet",repository.findAll(new Sort(Sort.Direction.ASC,key)));
		view.setViewName("index");
		return view;
	}
	
	@GetMapping("add_record")
	public ModelAndView add_record_get(@ModelAttribute("formModel")Student student, ModelAndView view)
	{
		view.setViewName("add_record");
		return view;
	}
	
	@GetMapping("delete_record/{id}")
	public ModelAndView delete_record_get
	(@PathVariable Long id, ModelAndView view)
	{
		view.addObject("record",repository.findById(id).get());
		view.setViewName("delete_record");
		return view;
	}
	@GetMapping("edit_record/{id}")
	public ModelAndView edit_record_get
	(@ModelAttribute("formModel")Student student,@PathVariable Long id,ModelAndView view) 
	{	
		view.addObject("record",repository.findById(id).get());
		view.setViewName("edit_record");
		return view;
	}
	//|---------------------------------------------------------------------------------------
	//|  POST method
	//|---------------------------------------------------------------------------------------
		
	@PostMapping("/add_record")
	public ModelAndView add_record_post
	(@ModelAttribute("formModel") @Validated Student student,BindingResult result,ModelAndView view) 
	{	
		if(!result.hasErrors()) {
			repository.saveAndFlush(student);
			return new ModelAndView("redirect:/");
		}else {
			view.setViewName("add_record");
			return view;
		}	
	}
	@PostMapping("/edit_record")
	public ModelAndView edit_record_post
	(@ModelAttribute("formModel")@Validated Student student,BindingResult result,ModelAndView view)
	{
		if(!result.hasErrors()) {
		repository.saveAndFlush(student);
		return new ModelAndView("redirect:/");
		}else {
			view.setViewName("edit_record");
			return view;
		}
		
	}
	@PostMapping("/delete_record")
	public ModelAndView delete_record_post
	(@RequestParam("id")Long id) 
	{
		repository.deleteById(id);
		return new ModelAndView("redirect:/");
	}
	

	
}
