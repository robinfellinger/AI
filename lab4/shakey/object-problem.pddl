; problem: shakey must place all objects in room 3
(define (problem problem-object)
	(:domain shakey)
	(:objects

		r1 r2 r3 - room
		b1 - box
		o1 o2 o3 o4 - object
		shakey - shakey
		g1 g2 - gripper
    d1 d2 d3 - door
	)

	(:init
		(adjacent r1 r2) (adjacent r2 r3)
    (adjacent r2 r1) (adjacent r3 r2)
		(connects d1 r1 r2) (connects d1 r2 r1)
    (connects d2 r2 r3) (connects d2 r3 r2)
    (connects d3 r2 r3) (connects d3 r3 r2)
    (wide d1) (wide d3)
		(shakey-in shakey r2)
		(box-in b1 r1)
		(object-in o1 r3) (object-in o2 r1) (object-in o3 r1)
		(object-in o4 r2)
		(empty g1) (empty g2)
	)

	(:goal
		(and
			(object-in o1 r3) (object-in o2 r3) (object-in o3 r3)
				(object-in o4 r3)
		)
	)
)
