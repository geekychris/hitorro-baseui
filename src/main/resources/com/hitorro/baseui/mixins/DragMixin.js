/**
 */

DragMixin = Class.create({

    initialize: function (dragId) {
        new Draggable(dragId, {
            revert: true
        });
    }

})

Tapestry.Initializer.dragMixin = function (spec) {
    new DragMixin(spec.dragId);
}

