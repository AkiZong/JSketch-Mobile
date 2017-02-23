Implemented a mobile version of JSketch for the Android platform.

**************************************  Description of the Vector Drawing:  **************************************

————————————————————


Tools: 
	- In terms of interface, a selected tool button will show with red border.

	- “selection”
		-> you cannot select anything when there is no shape on canvas(a message will show in “logcat window”).
		-> the border of selective shape will be dashed.
		-> the “thickness” and “color” will reflect current selected shape.
		-> if a shape is selected, you can move it around the screen.
		-> if you click the position outside of a selected shape, the shape will be unselected.
		-> if there are overlapping shapes, it will select the latest drawn shape.
		-> if a shape is selected and you choose a different color and/or thickness, the original shape border will be changed to the chosen color and/or thickness

	- “erase”
		-> you cannot delete anything when there is no shape on canvas (a message will show in “logcat window”).
		-> selected shape will be deleted.
		-> if there are overlapping shapes, it will delete the latest drawn shape.
		-> if you tap-and-hold the eraser button, you can clear the whole canvas.

	- “fill”
		-> you cannot fill anything when there is no shape on canvas (a message will show in “logcat window”).
		-> selected shape will be filled with chosen color.
		-> if there are overlapping shapes, it will fill the latest drawn shape.

	- “line”, “circle”, “rectangle”
		-> before drawing, choose one of them, the color will be RED by default.



Color:
	- In terms of interface, a selected color button will show with black border.



Thickness:
	- In terms of interface, a selected thickness button will show with red border.	
	- There are 4 level of thickness can be chosen.



*********************************************  Enhancements:  *********************************************

Undo/Redo

1. Tap undo button once, you can undo the latest action. You can undo at most 5 latest actions.
2. Tap and hold undo button, you can redo the latest undo action.
(use tap-and-hold effect since add one more button will make the interface be too crowded) 



***************************************  Development Environment:  ***************************************

Mac OS
Version 10.11.3
java 1.7.0_81 

Android 6.0 “Marshmallow” (API 23) is the minimum version

