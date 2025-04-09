package org.aturkov.expense.dao.specification;

import jakarta.persistence.criteria.*;
import org.aturkov.expense.domain.DataRange;
import org.aturkov.expense.domain.Ternary;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static org.aturkov.cambium.pagination.util.SpecificationUtils.getPredicate;

public class CommonSpecification<T> extends AbstractSpecification<T> {
//    /**
//     * Generates a Specification to check hierarchy of child elements based on the given parameters.
//     * The method supports filtering based on a list of UUIDs, negating the condition,
//     * including null values, and limiting the hierarchy depth.
//     *
//     * @param <T>               The type of the entities being queried.
//     * @param ids               A collection of UUIDs representing the hierarchy roots to validate against.
//     * @param not               A flag indicating whether to negate the result of the condition.
//     * @param includeNullValues A flag indicating whether null values should be included in the results.
//     * @param depthLimit        The maximum depth of the hierarchy to consider for the query. If null, defaults to 1.
//     * @param ltreeFieldPath    The path to the ltree field in the entity. Can be one or more strings representing a nested field path.
//     * @return A Specification object that can be used in a JPA Criteria query to apply the hierarchy child check based on the given parameters.
//     */
//    public static <T> Specification<T> checkHierarchyChilds(Collection<UUID> ids, boolean not,
//                                                            boolean includeNullValues, Integer depthLimit, final String... ltreeFieldPath) {
//
//        return (root, query, cb) -> {
//            if (org.cambium.common.util.CollectionUtils.isEmpty(ids))
//                return cb.conjunction();
//            var preparedDepthLimit = depthLimit == null ? 1 : depthLimit;
//            var preparedIds = LTreeUtils.findChildsLQuery(ids.stream().map(UUID::toString).collect(Collectors.toList()), Range.of(1, preparedDepthLimit));
//            Path<String> ltreePath = getFieldPath(root, includeNullValues ? JoinType.LEFT : JoinType.INNER, ltreeFieldPath);
//            Predicate idPredicate;
//            var ltreeIsInFunction = cb.function("hierarchy_check_lquery", Boolean.class, ltreePath, cb.literal(preparedIds));
//            if (not) {
//                idPredicate = cb.isFalse(ltreeIsInFunction);
//            } else {
//                idPredicate = cb.isTrue(ltreeIsInFunction);
//            }
//            if (includeNullValues) {
//                return cb.or(idPredicate, ltreePath.isNull());
//            } else {
//                return idPredicate;
//            }
//        };
//    }

//    /**
//     * Builds a JPA Specification to check if the hierarchy parent meets certain conditions,
//     * such as belonging to a specified set of IDs or satisfying additional criteria like depth limit or null value inclusion.
//     *
//     * @param ids               A collection of UUIDs representing the hierarchy parent IDs to filter against.
//     * @param not               A boolean indicating whether the result should exclude (`true`) or include (`false`) the IDs specified.
//     * @param includeNullValues A boolean indicating whether entities with null in the hierarchy field should be included in the results.
//     * @param depthLimit        An optional integer specifying the maximum depth to consider in the hierarchy traversal (default is 1 if null).
//     * @param ltreeFiled        The name of the field storing the hierarchy (ltree) path.
//     * @param ltreeRootIdFiled  The field representing the parent ID whose hierarchy is being queried.
//     * @param ltreeRootClass    The entity class used as the root for the subquery.
//     * @param ltreeRootPath     An array of strings representing the path to access fields in the ltree root entity.
//     * @return A JPA Specification that can be used to apply the hierarchy parent check with the provided criteria.
//     */
//    protected static <T> @NotNull Specification<T> checkHierarchyParent(Collection<UUID> ids, boolean not, boolean includeNullValues, Integer depthLimit, String ltreeFiled, String ltreeRootIdFiled, Class ltreeRootClass, String... ltreeRootPath) {
//        return (root, query, cb) -> {
//            if (CollectionUtils.isEmpty(ids))
//                return cb.conjunction();
//            var preparedDepthLimit = depthLimit == null ? 1 : depthLimit;
//            Path<UUID> classIdPath = getFieldPath(root, includeNullValues ? JoinType.LEFT : JoinType.INNER, concatArray(ltreeRootPath, ltreeRootIdFiled));
//            Subquery<UUID> subquery = query.subquery(UUID.class);
//            var subqueryRoot = subquery.from(ltreeRootClass);
//            subquery.select(cb.function("ltree_of_uuids_get_parents", UUID.class, subqueryRoot.get(ltreeFiled), cb.literal(preparedDepthLimit)));
//            subquery.where(subqueryRoot.get(ltreeRootIdFiled).in(new ArrayList<>(ids)));
//            Predicate idPredicate;
//            if (not) {
//                idPredicate = cb.not(classIdPath.in(subquery));
//            } else {
//                idPredicate = classIdPath.in(subquery);
//            }
//            if (includeNullValues) {
//                return cb.or(idPredicate, classIdPath.isNull());
//            } else {
//                return idPredicate;
//            }
//        };
//    }

//    public static <T> Specification<T> checkClassId(final Collection<UUID> twinClassUuids, String... twinEntityFieldPath) {
//        return (root, query, cb) -> {
//            if (CollectionUtils.isEmpty(twinClassUuids)) {
//                return cb.conjunction();
//            }
//            From fromTwin = getReducedRoot(root, JoinType.INNER, twinEntityFieldPath);
//            List<Predicate> predicates = twinClassUuids.stream().map(twinClassId -> cb.equal(fromTwin.get(TwinEntity.Fields.twinClassId), twinClassId)).toList();
//            return getPredicate(cb, predicates, true);
//        };
//    }

//    /**
//     * Creates a JPA {@code Specification} to filter entities based on specified twin class UUIDs, API user permissions,
//     * and optional twin entity field paths. This method dynamically builds predicates for domain, ownership,
//     * and twin class associations to ensure proper filtering and access control.
//     *
//     * @param <T>                 the type of entity for which the specification is created
//     * @param twinClassUuids      a collection of UUIDs representing specific twin classes to filter by; may be empty
//     *                            or null to apply broader access control filtering
//     * @param apiUser             an instance of {@code ApiUser} containing user, domain, and business account
//     *                            details for permission evaluation
//     * @param twinEntityFieldPath an optional array of strings representing the field path to navigate and join
//     *                            related twin entity fields
//     * @return a JPA {@code Specification} that filters entities based on twin class, ownership, and other criteria
//     * derived from the provided API user and inputs
//     * @throws ServiceException if there is an error during specification creation or validation
//     */
//    public static <T> Specification<T> checkClass(final Collection<UUID> twinClassUuids, final ApiUser apiUser, String... twinEntityFieldPath) throws ServiceException {
//        UUID finalUserId = apiUser.isUserSpecified() ? apiUser.getUserId() : null;
//        UUID finalBusinessAccountId = apiUser.isBusinessAccountSpecified() ? apiUser.getBusinessAccountId() : null;
//        UUID finalDomainId = apiUser.getDomainId();
//
//        return (root, query, cb) -> {
//            From fromTwin = getReducedRoot(root, JoinType.INNER, twinEntityFieldPath);
//            Join twinClass = fromTwin.join(TwinEntity.Fields.twinClass);
//            Predicate domain = cb.equal(twinClass.get(TwinClassEntity.Fields.domainId), finalDomainId);
//            List<Predicate> predicates = List.of(cb.conjunction());
//            if (!CollectionUtils.isEmpty(twinClassUuids))
//                predicates = twinClassUuids.stream().map(twinClassId -> cb.equal(fromTwin.get(TwinEntity.Fields.twinClassId), twinClassId)).toList();
//
//            Predicate joinPredicateSystemLevel = cb.equal(twinClass.get(TwinClassEntity.Fields.ownerType), SYSTEM);
//            Predicate joinPredicateUserLevel = cb.or(
//                    cb.equal(twinClass.get(TwinClassEntity.Fields.ownerType), USER),
//                    cb.equal(twinClass.get(TwinClassEntity.Fields.ownerType), DOMAIN_USER),
//                    cb.equal(twinClass.get(TwinClassEntity.Fields.ownerType), DOMAIN_BUSINESS_ACCOUNT_USER)
//            );
//            Predicate joinPredicateBusinessLevel = cb.or(
//                    cb.equal(twinClass.get(TwinClassEntity.Fields.ownerType), BUSINESS_ACCOUNT),
//                    cb.equal(twinClass.get(TwinClassEntity.Fields.ownerType), DOMAIN_BUSINESS_ACCOUNT),
//                    cb.equal(twinClass.get(TwinClassEntity.Fields.ownerType), DOMAIN_BUSINESS_ACCOUNT_USER)
//            );
//
//            Predicate rootPredicateUser = cb.equal(fromTwin.get(TwinEntity.Fields.ownerUserId), finalUserId);
//            Predicate rootPredicateBusiness = cb.equal(fromTwin.get(TwinEntity.Fields.ownerBusinessAccountId), finalBusinessAccountId);
//
//            return cb.and(domain,
//                    getPredicate(cb, predicates, true),
//                    cb.or(
//                            cb.and(joinPredicateUserLevel, rootPredicateUser),
//                            cb.and(joinPredicateBusinessLevel, rootPredicateBusiness)
//                            //todo system level:  add Subquery to detect valid user and business account twins
//                    )
//            );
//        }
//                ;
//    }

//    /**
//     * Creates a JPA {@code Specification} to check user permissions based on a given domain, business account,
//     * user details, and associated user groups. Dynamically generates joins and applies a custom SQL function
//     * for permission validation.
//     *
//     * @param <T>                 the type of the entity for which the specification is created
//     * @param domainId            the UUID of the domain within which the permissions are being checked
//     * @param businessAccountId   the UUID of the business account associated with the permissions
//     * @param userId              the UUID of the user for whom permissions are being checked
//     * @param userGroups          a set of UUIDs representing the groups the user belongs to
//     * @param twinEntityFieldPath an optional array of strings representing the path to navigate and join
//     *                            fields for the twin entity
//     * @return a JPA {@code Specification} that evaluates whether the user has the necessary permissions,
//     * based on the provided criteria
//     * @throws ServiceException if there is an error during specification creation or permission validation
//     */
//    public static <
//            T> Specification<T> checkPermissions(UUID domainId, UUID businessAccountId, UUID userId, Set<UUID> userGroups, String...
//            twinEntityFieldPath) throws ServiceException {
//        return (root, query, cb) -> {
//            From joinTwin = getReducedRoot(root, JoinType.INNER, twinEntityFieldPath);
//
//            Expression<UUID> spaceId = joinTwin.get(TwinEntity.Fields.permissionSchemaSpaceId);
//            Expression<UUID> permissionIdTwin = joinTwin.get(TwinEntity.Fields.viewPermissionId);
//            Expression<UUID> permissionIdTwinClass = joinTwin.join(TwinEntity.Fields.twinClass).get(TwinClassEntity.Fields.viewPermissionId);
//            Expression<UUID> twinClassId = joinTwin.join(TwinEntity.Fields.twinClass).get(TwinClassEntity.Fields.id);
//
//            Predicate isAssigneePredicate = cb.equal(joinTwin.get(TwinEntity.Fields.assignerUserId), cb.literal(userId));
//            Predicate isCreatorPredicate = cb.equal(joinTwin.get(TwinEntity.Fields.createdByUserId), cb.literal(userId));
//
//            return cb.isTrue(cb.function("permission_check", Boolean.class,
//                    cb.literal(domainId),
//                    cb.literal(businessAccountId),
//                    spaceId,
//                    permissionIdTwin,
//                    permissionIdTwinClass,
//                    cb.literal(userId),
//                    cb.literal(collectionUuidsToSqlArray(userGroups)),
//                    twinClassId,
//                    cb.selectCase().when(isAssigneePredicate, cb.literal(true)).otherwise(cb.literal(false)),
//                    cb.selectCase().when(isCreatorPredicate, cb.literal(true)).otherwise(cb.literal(false))
//            ));
//        };
//    }

//    /**
//     * Creates a JPA specification to filter entities based on a LocalDateTime field being between
//     * a specified range. Dynamically generates joins as needed for the provided field path and
//     * applies the filter conditions using a DataTimeRange object.
//     *
//     * @param <T>       the type of the entity for which the specification is created
//     * @param range     an object containing the starting and ending LocalDateTime values defining
//     *                  the range; can be null to apply no constraints
//     * @param filedPath the hierarchical path representing the fields to navigate and join,
//     *                  ending with the target field
//     * @return a JPA {@code Specification} matching entities where the field is between the
//     * specified range, or an unconstrained {@code Specification} if {@code range} is null
//     */
//    public static <T> Specification<T> checkFieldLocalDateTimeBetween(final DataTimeRange range, String...
//            filedPath) {
//        if (range == null) return (root, query, cb) -> cb.conjunction();
//        else return checkFieldLocalDateTimeBetween(range.getFrom(), range.getTo(), filedPath);
//    }

    public static <T> Specification<T> checkFieldLocalDateTimeBetween(final DataRange range, String...
            filedPath) {
        if (range == null) return (root, query, cb) -> cb.conjunction();
        else return checkFieldLocalDateTimeBetween(range.getFrom(), range.getTo(), filedPath);
    }

    /**
     * Creates a JPA specification to filter entities based on a LocalDateTime field being between a specified range.
     * It dynamically generates joins as needed for the provided field path and applies the filter conditions.
     *
     * @param <T>       the type of the entity for which the specification is created
     * @param from      the starting LocalDateTime for the range; if null, no lower bound is applied
     * @param to        the ending LocalDateTime for the range; if null, no upper bound is applied
     * @param filedPath the hierarchical path representing the fields to navigate and join, ending with the target field
     * @return a JPA {@code Specification} matching entities where the field is between the specified range,
     * or an unconstrained {@code Specification} if both {@code from} and {@code to} are null
     */
    public static <T> Specification<T> checkFieldLocalDateTimeBetween(final Timestamp from,
                                                                      final Timestamp to, String... filedPath) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (from == null && to == null) return predicate;
            if (from != null) {
                Predicate tmpPredicate = predicate;
                predicate = createPredicateWithJoins(root, cb, from, (property, criteriaBuilder, filedValue) -> criteriaBuilder.and(tmpPredicate, criteriaBuilder.greaterThanOrEqualTo(property, filedValue)), JoinType.INNER, filedPath);
            }
            if (to != null) {
                Predicate tmpPredicate = predicate;
                predicate = createPredicateWithJoins(root, cb, to, (property, criteriaBuilder, filedValue) -> criteriaBuilder.and(tmpPredicate, criteriaBuilder.lessThanOrEqualTo(property, filedValue)), JoinType.INNER, filedPath);
            }
            return predicate;
        };
    }

    public static <T> Specification<T> checkUuidIn(final Collection<UUID> uuids, boolean not,
                                                   boolean includeNullValues, final String... uuidFieldPath) {
        return (root, query, cb) -> {
            if (CollectionUtils.isEmpty(uuids)) return cb.conjunction();
            Path<UUID> fieldPath = getFieldPath(root, includeNullValues ? JoinType.LEFT : JoinType.INNER, uuidFieldPath);
            Predicate predicate = not ? fieldPath.in(uuids).not() : fieldPath.in(uuids);
            return includeNullValues ? cb.or(predicate, fieldPath.isNull()) : cb.and(predicate, fieldPath.isNotNull());
        };
    }

    public static <T> Specification<T> checkUuid(final UUID uuid, boolean not,
                                                 boolean includeNullValues, final String... uuidFieldPath) {
        return (root, query, cb) -> {
            if (uuid == null) return cb.conjunction();
            Path<UUID> fieldPath = getFieldPath(root, includeNullValues ? JoinType.LEFT : JoinType.INNER, uuidFieldPath);
            Predicate predicate = not ? cb.equal(fieldPath, uuid).not() : cb.equal(fieldPath, uuid);
            return includeNullValues ? cb.or(predicate, fieldPath.isNull()) : cb.and(predicate, fieldPath.isNotNull());
        };
    }

    /**
     * Creates a JPA specification to filter entities by checking if the string value of a specified field path
     * matches a given pattern using a "like" comparison. The method dynamically generates joins as needed for the
     * specified field path and applies the case-sensitive "like" operator on the target field.
     *
     * @param <T>        the type of the entity for which the specification is created
     * @param fieldValue the string value to be matched against the target field; if null, no comparison is applied
     * @param filedPath  the hierarchical path representing the fields to navigate and join, ending with the target field
     * @return a JPA {@code Specification} for filtering entities where the target field matches the given string value;
     * if the parameters are invalid, returns a specification that imposes no filter conditions
     */
    public static <T> Specification<T> checkFieldStringLike(String fieldValue, String... filedPath) {
        return (root, query, cb) -> createPredicateWithJoins(root, cb, fieldValue, (property, criteriaBuilder, filedValue) -> criteriaBuilder.like(property, filedValue), JoinType.INNER, filedPath);
    }

    /**
     * Creates a JPA specification to filter entities by verifying if a specified UUID matches the field
     * specified by the provided entity field path. The method dynamically generates joins if required
     * by the field path and applies the equality condition on the target field.
     *
     * @param <T>        the type of the entity for which the specification is created
     * @param fieldValue the UUID value to be compared against the target field
     * @param filedPath  the hierarchical path representing the fields to navigate and join,
     *                   ending with the target field
     * @return a JPA {@code Specification} matching entities where the specified UUID equals the target field;
     * null-safe and returns appropriate predicates even when field paths are invalid or null
     */
    public static <T> Specification<T> checkFieldUuid(UUID fieldValue, String... filedPath) {
        return (root, query, cb) -> createPredicateWithJoins(root, cb, fieldValue, (property, criteriaBuilder, filedValue) -> criteriaBuilder.equal(property, filedValue), JoinType.INNER, filedPath);
    }


    //    Use checkFieldLikeIn
    @Deprecated
    public static <T> Specification<T> checkFieldLikeContainsIn(final Collection<String> search,
                                                                final boolean not, final boolean or, final String... fieldPath) {
        return checkFieldLikeIn(CollectionUtils.isEmpty(search) ? search : search.stream().map(it -> "%" + it + "%").collect(Collectors.toSet()), not, or, fieldPath);
    }

    public static <T> Specification<T> checkFieldLikeIn(final Collection<String> search, final boolean not,
                                                        final boolean or, final String... fieldPath) {
        return checkFieldLikeIn(search, not, or, false, fieldPath);
    }

    /**
     * Creates a JPA {@code Specification} to filter entities by checking if values in a collection of search terms
     * match (using a "LIKE" condition) the specified field. The method generates predicates dynamically, allowing for
     * case-insensitive comparison and logical combinations using "AND" or "OR".
     *
     * @param <T>       the type of the entity for which the specification is created
     * @param search    a collection of strings to be matched against the target field
     * @param not       a boolean indicating whether to apply a "NOT LIKE" condition instead of "LIKE"
     * @param or        a boolean indicating whether multiple predicates should be combined with "OR" (if true) or "AND" (if false)
     * @param fieldPath a hierarchical path representing the fields to navigate and join, ending with the target field
     * @return a JPA {@code Specification} matching entities where the field's value satisfies the "LIKE" condition for any
     * or all search terms, based on the logical combination specified by the parameters
     */
    public static <T> Specification<T> checkFieldLikeIn(final Collection<String> search, final boolean not,
                                                        final boolean or, boolean includeNullValues, final String... fieldPath) {
        return (root, query, cb) -> {
            if (CollectionUtils.isEmpty(search))
                return cb.conjunction();

            List<Predicate> predicates = search.stream().map(name -> {
                Predicate predicate = cb.like(cb.lower(getFieldPath(root, includeNullValues ? JoinType.LEFT : JoinType.INNER, fieldPath)), name.toLowerCase());
                if (not) predicate = cb.not(predicate);
                return predicate;
            }).toList();
            return getPredicate(cb, predicates, or);
        };
    }

//    public static <T> Specification<T> checkFieldLongRange(
//            final LongRange range,
//            final String... fieldPath) {
//        return (root, query, cb) -> {
//            if (range == null || (range.getFrom() == null && range.getTo() == null)) {
//                return cb.conjunction();
//            }
//
//            List<Predicate> predicates = new ArrayList<>();
//            Path<Long> field = getFieldPath(root, JoinType.INNER, fieldPath);
//
//            if (range.getFrom() != null) {
//                predicates.add(cb.greaterThanOrEqualTo(field, range.getFrom()));
//            }
//            if (range.getTo() != null) {
//                predicates.add(cb.lessThanOrEqualTo(field, range.getTo()));
//            }
//
//            return cb.and(predicates.toArray(Predicate[]::new));
//        };
//    }

    public static <T> Specification<T> checkTernary(Ternary ternary, final String... fieldPath) {
        return (root, query, cb) -> {
            if (ternary == null)
                return cb.conjunction();
            return switch (ternary) {
                case ONLY -> cb.isTrue(getFieldPath(root, JoinType.INNER, fieldPath));
                case ONLY_NOT -> cb.isFalse(getFieldPath(root, JoinType.INNER, fieldPath));
                default -> cb.conjunction();
            };
        };
    }
}
