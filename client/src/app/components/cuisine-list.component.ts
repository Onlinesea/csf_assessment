import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { RestaurantService } from '../restaurant-service';

@Component({
  selector: 'app-cuisine-list',
  templateUrl: './cuisine-list.component.html',
  styleUrls: ['./cuisine-list.component.css']
})
export class CuisineListComponent implements OnInit{

  cuisineList:string[]=[];
	// TODO Task 2
	// For View 1

  constructor(private svc:RestaurantService, 
              private router:Router){}

  ngOnInit(): void {
      this.svc.getCuisineList().then(response =>{
        console.info(response)
      }).catch(error =>{
        console.error( error)
      })
  }



}
