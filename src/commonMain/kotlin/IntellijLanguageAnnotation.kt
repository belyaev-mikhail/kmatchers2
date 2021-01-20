package org.intellij.lang.annotations

import kotlin.annotation.AnnotationTarget.*

/**
 * Specifies that an element of the program is not a user-visible string which needs to be localized,
 * or does not contain such strings. The annotation is intended to be used by localization tools for
 * detecting strings which should not be reported as requiring localization.
 *
 *  * If a method parameter is annotated with `NonNls`, the strings passed
 * as values of this parameter are not reported as requiring localization.
 * Also, if the parameter of a property setter method is annotated with `NonNls`, values
 * of that property in UI Designer forms are never highlighted as hard-coded strings.
 *  * If a field is annotated with `NonNls`, all string literals found in the
 * initializer of the field are not reported as requiring localization.
 *  * If a method is called on a field, parameter or local variable annotated with `NonNls`,
 * string literals passed as parameters to the method are not reported as requiring localization.
 *  * If a field, parameter or local variable annotated with `NonNls` is passed as a
 * parameter to the `equals()` method invoked on a string literal, the literal is not
 * reported as requiring localization.
 *  * If a field, parameter or local variable annotated with `NonNls` is found at
 * the left side of an assignment expression, all string literals in the right side
 * of the expression are not reported as requiring localization.
 *  * If a method is annotated with `NonNls`, string literals returned from the method
 * are not reported as requiring localization.
 *  * If a class is annotated with `NonNls`, all string literals in
 * the class and all its subclasses are not reported as requiring localization.
 *  * If a package is annotated with `NonNls`, all string literals in
 * the package and all its subpackages are not reported as requiring localization.
 *
 *
 * @author max
 */
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(
    FUNCTION,
    PROPERTY_GETTER,
    PROPERTY_SETTER,
    FIELD,
    VALUE_PARAMETER,
    LOCAL_VARIABLE,
    ANNOTATION_CLASS,
    CLASS,
    FILE
)
annotation class NonNls

@Retention(AnnotationRetention.SOURCE)
@Target(
    FUNCTION,
    PROPERTY_GETTER,
    PROPERTY_SETTER,
    FIELD,
    VALUE_PARAMETER,
    LOCAL_VARIABLE,
    ANNOTATION_CLASS,
    TYPE,
    EXPRESSION
)
annotation class Language(@NonNls val value: String, @NonNls val prefix: String = "", @NonNls val suffix: String = "")
