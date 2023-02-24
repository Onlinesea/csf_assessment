// Do not change these interfaces
export interface Restaurant {
	restaurantId: string
	//change namd to name and susisine to cuisine
	name: string
	cuisine: string
	address: string
	coordinates: number[]
}

export interface Comment {
	name: string
	rating: number
	restaurantId: string
	text: string
}
