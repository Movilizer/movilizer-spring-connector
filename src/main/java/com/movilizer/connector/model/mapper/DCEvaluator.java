package com.movilizer.connector.model.mapper;

/**
 * This interface should be implemented by BL that needs to evaluate specific DataContainers. So if a speicifc DataContainer is evaluated by one BL class this class should implement the interface.
 *
 * @author Pavel Kotlov
 *
 * @param <E>
 */
public interface DCEvaluator<E> {

    public Class<E> getMappingClass();

    public boolean perform(E object);

}
