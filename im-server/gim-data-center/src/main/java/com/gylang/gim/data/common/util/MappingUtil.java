package com.gylang.gim.data.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author gylang
 * data 2021/1/8
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MappingUtil {

    private static final MapperFacade MAPPERFACADE = new DefaultMapperFactory.Builder().mapNulls(false).build().getMapperFacade();


    public static <S, D> D map(S sourceObject, Class<D> destinationClass) {
        return MAPPERFACADE.map(sourceObject, destinationClass);
    }


    public static <S, D> D map(S sourceObject, Class<D> destinationClass, MappingContext context) {
        return MAPPERFACADE.map(sourceObject, destinationClass, context);
    }


    public static <S, D> D map(S sourceObject, D destinationObject) {
        MAPPERFACADE.map(sourceObject, destinationObject);
        return destinationObject;
    }


    public static <S, D> void map(S sourceObject, D destinationObject, MappingContext context) {
        MAPPERFACADE.map(sourceObject, destinationObject, context);
    }


    public static <S, D> void map(S sourceObject, D destinationObject, Type<S> sourceType, Type<D> destinationType) {
        MAPPERFACADE.map(sourceObject, destinationObject, sourceType, destinationType);
    }


    public static <S, D> void map(S sourceObject, D destinationObject, Type<S> sourceType, Type<D> destinationType, MappingContext context) {

        MAPPERFACADE.map(sourceObject, destinationObject, sourceType, destinationType, context);
    }


    public static <S, D> Set<D> mapAsSet(Iterable<S> source, Class<D> destinationClass) {
        return MAPPERFACADE.mapAsSet(source, destinationClass);
    }


    public static <S, D> Set<D> mapAsSet(Iterable<S> source, Class<D> destinationClass, MappingContext context) {
        return MAPPERFACADE.mapAsSet(source, destinationClass, context);
    }


    public static <S, D> Set<D> mapAsSet(S[] source, Class<D> destinationClass) {
        return MAPPERFACADE.mapAsSet(source, destinationClass);
    }


    public static <S, D> Set<D> mapAsSet(S[] source, Class<D> destinationClass, MappingContext context) {
        return MAPPERFACADE.mapAsSet(source, destinationClass, context);
    }


    public static <S, D> List<D> mapAsList(Iterable<S> source, Class<D> destinationClass) {
        return MAPPERFACADE.mapAsList(source, destinationClass);
    }


    public static <S, D> List<D> mapAsList(Iterable<S> source, Class<D> destinationClass, MappingContext context) {
        return MAPPERFACADE.mapAsList(source, destinationClass, context);
    }


    public static <S, D> List<D> mapAsList(S[] source, Class<D> destinationClass) {
        return MAPPERFACADE.mapAsList(source, destinationClass);
    }


    public static <S, D> List<D> mapAsList(S[] source, Class<D> destinationClass, MappingContext context) {
        return MAPPERFACADE.mapAsList(source, destinationClass, context);
    }


    public static <S, D> D[] mapAsArray(D[] destination, Iterable<S> source, Class<D> destinationClass) {
        return MAPPERFACADE.mapAsArray(destination, source, destinationClass);
    }


    public static <S, D> D[] mapAsArray(D[] destination, S[] source, Class<D> destinationClass) {
        return MAPPERFACADE.mapAsArray(destination, source, destinationClass);
    }


    public static <S, D> D[] mapAsArray(D[] destination, Iterable<S> source, Class<D> destinationClass, MappingContext context) {
        return MAPPERFACADE.mapAsArray(destination, source, destinationClass, context);
    }


    public static <S, D> D[] mapAsArray(D[] destination, S[] source, Class<D> destinationClass, MappingContext context) {
        return MAPPERFACADE.mapAsArray(destination, source, destinationClass, context);
    }


    public static <S, D> void mapAsCollection(Iterable<S> source, Collection<D> destination, Class<D> destinationClass) {
        MAPPERFACADE.mapAsCollection(source, destination, destinationClass);
    }


    public static <S, D> void mapAsCollection(Iterable<S> source, Collection<D> destination, Class<D> destinationClass, MappingContext context) {
        MAPPERFACADE.mapAsCollection(source, destination, destinationClass, context);
    }


    public static <S, D> void mapAsCollection(S[] source, Collection<D> destination, Class<D> destinationClass) {
        MAPPERFACADE.mapAsCollection(source, destination, destinationClass);
    }


    public static <S, D> void mapAsCollection(S[] source, Collection<D> destination, Class<D> destinationClass, MappingContext context) {
        MAPPERFACADE.mapAsCollection(source, destination, destinationClass, context);
    }


    public static <S, D> D map(S sourceObject, Type<S> sourceType, Type<D> destinationType) {
        return MAPPERFACADE.map(sourceObject, sourceType, destinationType);
    }


    public static <S, D> D map(S sourceObject, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return MAPPERFACADE.map(sourceObject, sourceType, destinationType, context);
    }


    public static <S, D> Set<D> mapAsSet(Iterable<S> source, Type<S> sourceType, Type<D> destinationType) {
        return MAPPERFACADE.mapAsSet(source, sourceType, destinationType);
    }


    public static <S, D> Set<D> mapAsSet(Iterable<S> source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return MAPPERFACADE.mapAsSet(source, sourceType, destinationType, context);
    }


    public static <S, D> Set<D> mapAsSet(S[] source, Type<S> sourceType, Type<D> destinationType) {
        return MAPPERFACADE.mapAsSet(source, sourceType, destinationType);
    }


    public static <S, D> Set<D> mapAsSet(S[] source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return MAPPERFACADE.mapAsSet(source, sourceType, destinationType, context);
    }


    public static <S, D> List<D> mapAsList(Iterable<S> source, Type<S> sourceType, Type<D> destinationType) {
        return MAPPERFACADE.mapAsList(source, sourceType, destinationType);
    }


    public static <S, D> List<D> mapAsList(Iterable<S> source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return MAPPERFACADE.mapAsList(source, sourceType, destinationType, context);
    }


    public static <S, D> List<D> mapAsList(S[] source, Type<S> sourceType, Type<D> destinationType) {
        return MAPPERFACADE.mapAsList(source, sourceType, destinationType);
    }


    public static <S, D> List<D> mapAsList(S[] source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return MAPPERFACADE.mapAsList(source, sourceType, destinationType, context);
    }


    public static <S, D> D[] mapAsArray(D[] destination, Iterable<S> source, Type<S> sourceType, Type<D> destinationType) {
        return MAPPERFACADE.mapAsArray(destination, source, sourceType, destinationType);
    }


    public static <S, D> D[] mapAsArray(D[] destination, S[] source, Type<S> sourceType, Type<D> destinationType) {
        return MAPPERFACADE.mapAsArray(destination, source, sourceType, destinationType);
    }


    public static <S, D> D[] mapAsArray(D[] destination, Iterable<S> source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return MAPPERFACADE.mapAsArray(destination, source, sourceType, destinationType, context);
    }


    public static <S, D> D[] mapAsArray(D[] destination, S[] source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return MAPPERFACADE.mapAsArray(destination, source, sourceType, destinationType, context);
    }


    public static <S, D> void mapAsCollection(Iterable<S> source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType) {
        MAPPERFACADE.mapAsCollection(source, destination, sourceType, destinationType);
    }


    public static <S, D> void mapAsCollection(Iterable<S> source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        MAPPERFACADE.mapAsCollection(source, destination, sourceType, destinationType, context);
    }


    public static <S, D> void mapAsCollection(S[] source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType) {
        MAPPERFACADE.mapAsCollection(source, destination, sourceType, destinationType);
    }


    public static <S, D> void mapAsCollection(S[] source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        MAPPERFACADE.mapAsCollection(source, destination, sourceType, destinationType, context);
    }


    public static <S, D> D convert(S source, Class<D> destinationClass, String converterId, MappingContext mappingContext) {
        return MAPPERFACADE.convert(source, destinationClass, converterId, mappingContext);
    }


    public static <S, D> D convert(S source, Type<S> sourceType, Type<D> destinationType, String converterId, MappingContext mappingContext) {
        return MAPPERFACADE.convert(source, sourceType, destinationType, converterId, mappingContext);
    }


    public static <SK, SV, DK, DV> Map<DK, DV> mapAsMap(Map<SK, SV> source, Type<? extends Map<SK, SV>> sourceType, Type<? extends Map<DK, DV>> destinationType) {
        return MAPPERFACADE.mapAsMap(source, sourceType, destinationType);
    }


    public static <SK, SV, DK, DV> Map<DK, DV> mapAsMap(Map<SK, SV> source, Type<? extends Map<SK, SV>> sourceType, Type<? extends Map<DK, DV>> destinationType, MappingContext context) {
        return MAPPERFACADE.mapAsMap(source, sourceType, destinationType, context);
    }


    public static <S, DK, DV> Map<DK, DV> mapAsMap(Iterable<S> source, Type<S> sourceType, Type<? extends Map<DK, DV>> destinationType) {
        return MAPPERFACADE.mapAsMap(source, sourceType, destinationType);
    }


    public static <S, DK, DV> Map<DK, DV> mapAsMap(Iterable<S> source, Type<S> sourceType, Type<? extends Map<DK, DV>> destinationType, MappingContext context) {
        return MAPPERFACADE.mapAsMap(source, sourceType, destinationType, context);
    }


    public static <S, DK, DV> Map<DK, DV> mapAsMap(S[] source, Type<S> sourceType, Type<? extends Map<DK, DV>> destinationType) {
        return MAPPERFACADE.mapAsMap(source, sourceType, destinationType);
    }


    public static <S, DK, DV> Map<DK, DV> mapAsMap(S[] source, Type<S> sourceType, Type<? extends Map<DK, DV>> destinationType, MappingContext context) {
        return MAPPERFACADE.mapAsMap(source, sourceType, destinationType, context);
    }


    public static <SK, SV, D> List<D> mapAsList(Map<SK, SV> source, Type<? extends Map<SK, SV>> sourceType, Type<D> destinationType) {
        return MAPPERFACADE.mapAsList(source, sourceType, destinationType);
    }


    public static <SK, SV, D> List<D> mapAsList(Map<SK, SV> source, Type<? extends Map<SK, SV>> sourceType, Type<D> destinationType, MappingContext context) {
        return MAPPERFACADE.mapAsList(source, sourceType, destinationType, context);
    }


    public static <SK, SV, D> Set<D> mapAsSet(Map<SK, SV> source, Type<? extends Map<SK, SV>> sourceType, Type<D> destinationType) {
        return MAPPERFACADE.mapAsSet(source, sourceType, destinationType);
    }


    public static <SK, SV, D> Set<D> mapAsSet(Map<SK, SV> source, Type<? extends Map<SK, SV>> sourceType, Type<D> destinationType, MappingContext context) {
        return MAPPERFACADE.mapAsSet(source, sourceType, destinationType, context);
    }


    public static <SK, SV, D> D[] mapAsArray(D[] destination, Map<SK, SV> source, Type<? extends Map<SK, SV>> sourceType, Type<D> destinationType) {
        return MAPPERFACADE.mapAsArray(destination, source, sourceType, destinationType);
    }


    public static <SK, SV, D> D[] mapAsArray(D[] destination, Map<SK, SV> source, Type<? extends Map<SK, SV>> sourceType, Type<D> destinationType, MappingContext context) {
        return MAPPERFACADE.mapAsArray(destination, source, sourceType, destinationType, context);
    }


    public static <S, D> D newObject(S source, Type<? extends D> destinationType, MappingContext context) {
        return MAPPERFACADE.newObject(source, destinationType, context);
    }


}
