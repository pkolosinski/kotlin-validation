package dev.pkolosinski.kotlinvalidation.rules

import dev.pkolosinski.kotlinvalidation.ValidationError
import dev.pkolosinski.kotlinvalidation.buildValidator
import dev.pkolosinski.kotlinvalidation.shouldBeInvalid
import dev.pkolosinski.kotlinvalidation.shouldBeValid
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equals.shouldEqual

class CollectionElementValidationSpec : ShouldSpec({
    data class Item(val value: String?)

    data class TestedClass(
        val list: List<Item?>? = null,
        val set: Set<Item?>? = null,
        val map: Map<String, Item?>? = null,
        val strings: List<String?>? = null,
    )

    context("validateEach") {
        val validator = buildValidator {
            TestedClass::list.validateEach {
                Item::value.isNotBlank(errorCode = "ITEM_VALUE_BLANK")
            }
        }

        should("treat null and empty collections as valid") {
            validator(TestedClass(list = null)).shouldBeValid()
            validator(TestedClass(list = emptyList())).shouldBeValid()
        }

        should("include the index in every invalid element path") {
            val result = validator(
                TestedClass(
                    list = listOf(
                        Item(""),
                        Item("valid"),
                        Item(" "),
                        null,
                        Item(null),
                    ),
                ),
            )

            result.shouldBeInvalid()
                .errors.shouldHaveSize(2)
                .shouldContainExactlyInAnyOrder(
                    ValidationError(path = "list[0].value", errorCode = "ITEM_VALUE_BLANK"),
                    ValidationError(path = "list[2].value", errorCode = "ITEM_VALUE_BLANK"),
                )
        }
    }

    context("validateEach with fail-fast") {
        val validator = buildValidator(failFast = true) {
            TestedClass::list.validateEach {
                Item::value.isNotBlank(errorCode = "ITEM_VALUE_BLANK")
            }
        }

        should("return only the first invalid element error") {
            val result = validator(
                TestedClass(
                    list = listOf(
                        Item("value"),
                        Item(""),
                        Item("   "),
                    ),
                ),
            )

            result.shouldBeInvalid()
                .errors.shouldHaveSize(1)
                .first().shouldEqual(
                    ValidationError(path = "list[1].value", errorCode = "ITEM_VALUE_BLANK"),
                )
        }
    }

    context("validateEach with a set") {
        val validator = buildValidator {
            TestedClass::set.validateEach {
                Item::value.isNotBlank(errorCode = "ITEM_VALUE_BLANK")
            }
        }

        should("treat null and empty collections as valid") {
            validator(TestedClass(set = null)).shouldBeValid()
            validator(TestedClass(set = emptySet())).shouldBeValid()
        }

        should("include the iteration index in an invalid element path") {
            val result = validator(
                TestedClass(
                    set = linkedSetOf(
                        Item(""),
                        Item("valid"),
                    ),
                ),
            )

            result.shouldBeInvalid()
                .errors.shouldHaveSize(1)
                .first().shouldEqual(
                    ValidationError(path = "set[0].value", errorCode = "ITEM_VALUE_BLANK"),
                )
        }
    }

    context("validateValues") {
        val validator = buildValidator {
            TestedClass::map.validateValues {
                Item::value.isNotBlank(errorCode = "ITEM_VALUE_BLANK")
            }
        }

        should("treat null and empty collections as valid") {
            validator(TestedClass(map = null)).shouldBeValid()
            validator(TestedClass(map = emptyMap())).shouldBeValid()
        }

        should("include the map key in an invalid element path") {
            val result = validator(
                TestedClass(
                    map = mapOf(
                        "first" to Item(""),
                        "second" to Item("valid"),
                        "third" to null,
                        "fourth" to Item(null),
                    ),
                ),
            )

            result.shouldBeInvalid()
                .errors.shouldHaveSize(1)
                .first().shouldEqual(
                    ValidationError(path = "map[first].value", errorCode = "ITEM_VALUE_BLANK"),
                )
        }

        should("return only the first invalid value error when fail-fast is enabled") {
            val failFastValidator = buildValidator(failFast = true) {
                TestedClass::map.validateValues {
                    Item::value.isNotBlank(errorCode = "ITEM_VALUE_BLANK")
                }
            }
            val result = failFastValidator(
                TestedClass(
                    map = linkedMapOf(
                        "first" to Item(""),
                        "second" to Item(""),
                    ),
                ),
            )

            result.shouldBeInvalid()
                .errors.shouldHaveSize(1)
                .first().shouldEqual(
                    ValidationError(path = "map[first].value", errorCode = "ITEM_VALUE_BLANK"),
                )
        }
    }

    context("eachSatisfies for map values") {
        val validator = buildValidator {
            TestedClass::map.eachSatisfies(errorCode = "ITEM_VALUE_BLANK") { (key, value) ->
                value?.value?.isNotBlank() == true
            }
        }

        should("include the map key in every invalid value path") {
            val result = validator(
                TestedClass(
                    map = linkedMapOf(
                        "first" to Item(""),
                        "second" to Item("valid"),
                        "third" to Item(" "),
                    ),
                ),
            )

            result.shouldBeInvalid()
                .errors.shouldHaveSize(2)
                .shouldContainExactlyInAnyOrder(
                    ValidationError(path = "map[first]", errorCode = "ITEM_VALUE_BLANK"),
                    ValidationError(path = "map[third]", errorCode = "ITEM_VALUE_BLANK"),
                )
        }

        should("return only the first invalid value when fail-fast is enabled") {
            val failFastValidator = buildValidator(failFast = true) {
                TestedClass::map.eachSatisfies(errorCode = "ITEM_VALUE_BLANK") { (key, value) ->
                    value?.value?.isNotBlank() == true
                }
            }
            val result = failFastValidator(
                TestedClass(
                    map = linkedMapOf(
                        "first" to Item(""),
                        "second" to Item(""),
                    ),
                ),
            )

            result.shouldBeInvalid()
                .errors.shouldHaveSize(1)
                .first().shouldEqual(
                    ValidationError(path = "map[first]", errorCode = "ITEM_VALUE_BLANK"),
                )
        }
    }

    context("validateKeys") {
        val validator = buildValidator {
            TestedClass::map.validateKeys {
                ensure(errorCode = "KEY_FORMAT") {
                    it.startsWith("key-")
                }
            }
        }

        should("treat null and empty maps as valid") {
            validator(TestedClass(map = null)).shouldBeValid()
            validator(TestedClass(map = emptyMap())).shouldBeValid()
        }

        should("include the map key in an invalid key path") {
            val result = validator(
                TestedClass(
                    map = linkedMapOf(
                        "bad" to Item("valid"),
                        "key-good" to Item("valid"),
                    ),
                ),
            )

            result.shouldBeInvalid()
                .errors.shouldHaveSize(1)
                .first().shouldEqual(
                    ValidationError(path = "map[bad]", errorCode = "KEY_FORMAT"),
                )
        }

        should("return only the first invalid key when fail-fast is enabled") {
            val failFastValidator = buildValidator(failFast = true) {
                TestedClass::map.validateKeys {
                    ensure(errorCode = "KEY_FORMAT") {
                        it.startsWith("key-")
                    }
                }
            }
            val result = failFastValidator(
                TestedClass(
                    map = linkedMapOf(
                        "bad-first" to Item("valid"),
                        "bad-second" to Item("valid"),
                    ),
                ),
            )

            result.shouldBeInvalid()
                .errors.shouldHaveSize(1)
                .first().shouldEqual(
                    ValidationError(path = "map[bad-first]", errorCode = "KEY_FORMAT"),
                )
        }
    }

    context("validateEntries") {
        val validator = buildValidator {
            TestedClass::map.validateEntries {
                Map.Entry<String, Item?>::key.satisfies(
                    errorCode = "KEY_FORMAT",
                ) {
                    it.startsWith("key-")
                }
                Map.Entry<String, Item?>::value.isNotNull(
                    errorCode = "ITEM_REQUIRED",
                )
                Map.Entry<String, Item?>::value.validate {
                    Item::value.isNotBlank(errorCode = "ITEM_VALUE_BLANK")
                }
            }
        }

        should("validate nullable key and value members explicitly") {
            val result = validator(
                TestedClass(
                    map = linkedMapOf(
                        "bad" to null,
                        "key-blank" to Item(""),
                        "key-good" to Item("valid"),
                    ),
                ),
            )

            result.shouldBeInvalid()
                .errors.shouldHaveSize(3)
                .shouldContainExactlyInAnyOrder(
                    ValidationError(path = "map[bad].key", errorCode = "KEY_FORMAT"),
                    ValidationError(path = "map[bad].value", errorCode = "ITEM_REQUIRED"),
                    ValidationError(
                        path = "map[key-blank].value.value",
                        errorCode = "ITEM_VALUE_BLANK",
                    ),
                )
        }

        should("return only the first entry error when fail-fast is enabled") {
            val failFastValidator = buildValidator(failFast = true) {
                TestedClass::map.validateEntries {
                    Map.Entry<String, Item?>::key.satisfies(
                        errorCode = "KEY_FORMAT",
                    ) {
                        it.startsWith("key-")
                    }
                    Map.Entry<String, Item?>::value.isNotNull(
                        errorCode = "ITEM_REQUIRED",
                    )
                }
            }
            val result = failFastValidator(
                TestedClass(
                    map = linkedMapOf(
                        "bad-first" to null,
                        "bad-second" to Item("valid"),
                    ),
                ),
            )

            result.shouldBeInvalid()
                .errors.shouldHaveSize(1)
                .first().shouldEqual(
                    ValidationError(path = "map[bad-first].key", errorCode = "KEY_FORMAT"),
                )
        }
    }

    context("eachSatisfies") {
        val validator = buildValidator {
            TestedClass::strings.eachSatisfies(errorCode = "ITEM_VALUE_BLANK") {
                !it.isNullOrBlank()
            }
        }

        should("treat null and empty collections as valid") {
            validator(TestedClass(strings = null)).shouldBeValid()
            validator(TestedClass(strings = emptyList())).shouldBeValid()
        }

        should("include the index in every invalid element path") {
            val result = validator(
                TestedClass(
                    strings = listOf(
                        "",
                        "valid",
                        " ",
                    ),
                ),
            )

            result.shouldBeInvalid()
                .errors.shouldHaveSize(2)
                .shouldContainExactly(
                    ValidationError(path = "strings[0]", errorCode = "ITEM_VALUE_BLANK"),
                    ValidationError(path = "strings[2]", errorCode = "ITEM_VALUE_BLANK"),
                )
        }
    }
})
