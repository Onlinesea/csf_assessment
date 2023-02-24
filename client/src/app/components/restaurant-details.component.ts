import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Restaurant } from '../models';
import { RestaurantService } from '../restaurant-service';

@Component({
  selector: 'app-restaurant-details',
  templateUrl: './restaurant-details.component.html',
  styleUrls: ['./restaurant-details.component.css']
})
export class RestaurantDetailsComponent {
	
	// TODO Task 4 and Task 5
	// For View 3
  commentForm!:FormGroup
  restaurant !: Restaurant
  restaurantId!:string 
	// TODO Task 3
	// For View 2
  constructor(private activatedRoute:ActivatedRoute,
              private router:Router,
              private fb:FormBuilder,
              private svc:RestaurantService){}


    ngOnInit(): void {

      this.restaurantId=this.activatedRoute.snapshot.params['name']
      console.info(this.restaurantId)
      this.svc.getRestaurant(this.restaurantId).then(response =>{
        console.info(response)
        this.restaurant=response
      }).catch(error =>{
        console.error('error > ' + error)
      })
      this.commentForm=this.createForm();
    }

    private createForm(): FormGroup {
      return this.fb.group({
        name: this.fb.control<string>('', [ Validators.required] ),
        rating: this.fb.control<number>(0, [ Validators.required]),
        text: this.fb.control<string>('', [ Validators.required] )
      })
    }

    processComment(){
      let c: Comment ={
        name: this.commentForm.value['name'],
        rating: this.commentForm.value['rating'],
        resturantId:this.restaurantId,
        text:this.commentForm.value['text']

      }
      this.svc.postComment(c).then(response =>{
        console.info(response)
        this.router.navigate([`/`])
      }).catch(error =>{
        console.error('error > ' + error)
      })
    }
}
