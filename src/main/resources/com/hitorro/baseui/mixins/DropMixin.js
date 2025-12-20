/**
 * Created by IntelliJ IDEA.
 * User: Chris
 * Date: 11/10/11
 * Time: 2:04 PM
 * To change this template use File | Settings | File Templates.
 */
DropMixin = Class.create({

    initialize: function (spec) {
        this.options = spec;
        this.url = spec.url;
        this.dropC = spec.dropClass;
        this.did = spec.dropId;
        Droppables.add(spec.dropId, {
            accept: this.dropC,
            hoverclass: 'hover',
            onDrop: function (dropped) {
                $(spec.dropId).highlight();
                var entry = encodeURIComponent(spec.paramName) + '=' + encodeURIComponent(dropped.id);
                spec.parameters = spec.callback ?
                    spec.callback(spec.elementId, entry) : entry;

                if (spec.defaultParams)
                    spec.parameters += '&' + spec.defaultParams;
                new Ajax.Request(spec.url, spec);
            }
        });
    }

})

Tapestry.Initializer.dropMixin = function (spec) {
    new DropMixin(spec);
}
