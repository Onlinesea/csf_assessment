import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Restaurant } from '../models';
import { RestaurantService } from '../restaurant-service';

@Component({
  selector: 'app-restaurant-cuisine',
  templateUrl: './restaurant-cuisine.component.html',
  styleUrls: ['./restaurant-cuisine.component.css']
})
export class RestaurantCuisineComponent {
	
  restaurants: string[]=[]
  cuisine!:string 
	// TODO Task 3
	// For View 2
  constructor(private activatedRoute:ActivatedRoute,
              private router:Router,
              private svc:RestaurantService){}


    ngOnInit(): void {

      this.cuisine=this.activatedRoute.snapshot.params['cuisine']
      console.info(this.cuisine)
      this.svc.getRestaurantsByCuisine(this.cuisine).then(response =>{
        console.info(response)
        this.restaurants=response
      }).catch(error =>{
        console.error('error > ' + error)
      })
  }            
}
