; problem: shakey must light all the rooms
(define (problem problem-light)
	(:domain shakey)
	(:objects
		r1 r2 r3 	- room
		b1 			- box
		shakey 		- shakey
		g1 g2 		- gripper
    d1 d2 d3 - door
	)

	(:init
		(adjacent r1 r2) (adjacent r2 r3) (adjacent r3 r2) (adjacent r2 r1)
		(wide d1) (wide d3)
    (connects d1 r1 r2) (connects d2 r2 r3) (connects d3 r2 r3)
    (connects d1 r2 r1) (connects d2 r3 r2) (connects d3 r3 r2)
		(shakey-in shakey r2)
		(box-in b1 r1)
	)

	(:goal
		(and
			(lit r1) (lit r2) (lit r3)
		)
	)
)
