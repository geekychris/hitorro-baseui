// A TextboxHint observes a field, giving it a "textbox hint" when it is empty and does not have focus.
// Beware: it is flawed because it won't allow the user to submit text that is the same as the hint!
// Written in Protoype style because Tapestry includes the Protoype library (http://www.prototypejs.org/).

Tapestry.Initializer.beckDragDrop = function (spec) {
    alert('in initializer');
    new BeckDragDrop(spec.draggable, spec.droppable);
}

BeckDragDrop = Class.create({

        initialize: function (draggable, droppable) {
            try {
                alert('in init function');
                Droppables.add(droppable, {
                    accept: 'draggable',
                    hoverclass: 'hover',
                    onDrop: function () {
                        $(droppable).highlight();
                    }
                });
            } catch (err) {
                alert(err);
            }
            new Draggable(draggable, {
                revert: true
            });
        }
    }
)

// Extend the Tapestry.Initializer with a static method that instantiates a DragDrop.



