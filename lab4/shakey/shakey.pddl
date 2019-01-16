(define( domain shakey)
  (:requirements :strips :equality :typing :adl)


  (:types
    box ; a box, shakey can push (trough wide doors)
    shakey ; the robot himself
    room ; a room in the domain, can "contain" things
    object ; objects, shakey can grab
    gripper ; shakey's robot arms to pick objects up
    door ; door between rooms
  )
  ; we created the shakey's world
  ; shakey is a roboter with two grippers
  ; he can grab objects and push boxes through doors
  ; doors connect rooms

  (:predicates
    (shakey-in   ?s - shakey ?r - room)     ; shakey s is in room r
    (box-in      ?b - box ?r - room)        ; box b is in room r
    (object-in   ?o - object ?r - room)     ; object o is in room r
    (holding     ?o - object ?g - gripper)  ; gripper g holds object o
    (empty       ?g - gripper)              ; gripper g is empty
    (has-gripper ?s - shakey ?g - gripper)  ; shakey s has gripper g
    (lit         ?r - room)                 ; room r is lit
    (adjacent    ?r1 ?r2 - room)            ; room r1 and r2 are adjacent
    (wide        ?d - door)                 ; door d is wide
    (connects    ?d - door ?r1 ?r2 - room)  ; door d connects room r1 and r2
  )
  ; shakey, boxes and objects are positioned in a room
  ; when an object is grapped, it "leaves" the room and is positioned in the gripper
  ; door can be specified as "wide", because boxes fit only through wide doors
  ; a door connects to rooms with each other

  ; moves shakey from one room to another, they have to be adjacent
  (:action move
    :parameters (?s - shakey ?from ?to - room)
    :precondition (and
                    (shakey-in ?s ?from)
                    (adjacent ?from ?to)
                  )
    :effect (and
              (shakey-in ?s ?to)
              (not (shakey-in ?s ?from))
            )
  )

  ; shakey pushes a box from one room to another, they have to be connected trough a wide door
  (:action push
		:parameters (?s - shakey ?b - box ?from ?to - room ?d - door)
		:precondition (and
						        (wide ?d)
                    (connects ?d ?from ?to)
						        (box-in ?b ?from)
						        (shakey-in ?s ?from)
					        )

		:effect (and
		          (box-in ?b ?to)
					    (not (box-in ?b ?from))
					    (shakey-in ?s ?to)
					    (not (shakey-in ?s ?from))
				    )
	)

  ; shakey puts on the light in room, therefore he needs the box
  (:action switch-on
		:parameters (?s - shakey ?b - box ?r - room)
		:precondition (and
						        (shakey-in ?s ?r)
						        (box-in ?b ?r)
						        (not (lit ?r))
					        )

		:effect (lit ?r)
	)

  ; shakey puts off the light in room, therefore he needs the box
  (:action switch-off
		:parameters (?s - shakey ?b - box ?r - room)
		:precondition (and
						        (shakey-in ?s ?r)
						        (box-in ?b ?r)
					        )

		:effect (not (lit ?r))
	)

  ; shakey picks up an object with a gripper
  (:action pick-up
		:parameters (?s - shakey ?o - object ?g - gripper ?r - room)
		:precondition (and
						        (shakey-in ?s ?r)
						        (object-in ?o ?r)
						        (lit ?r)
						        (empty ?g)
					)

		:effect (and
					    (not (empty ?g))
				      (holding ?o ?g)
					    (not (object-in ?o ?r))
				    )
	)

  ; shakey puts down a object
  (:action put-down
		:parameters (?s - shakey ?o - object ?g - gripper ?r - room)
		:precondition (and
						(shakey-in ?s ?r)
						(holding ?o ?g)
					)

		:effect (and
					    (empty ?g)
					    (not (holding ?o ?g))
					    (object-in ?o ?r)
				)
	)
)
