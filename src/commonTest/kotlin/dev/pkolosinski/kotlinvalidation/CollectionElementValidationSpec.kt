package dev.pkolosinski.kotlinvalidation

import dev.pkolosinski.kotlinvalidation.ValidationResult.Invalid
import dev.pkolosinski.kotlinvalidation.ValidationResult.Valid
import dev.pkolosinski.kotlinvalidation.rules.isNotBlank
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class CollectionElementValidationSpec : ShouldSpec({
    data class Item(val value: String?)

    data class TestedClass(
        val list: List<Item?>? = null,
        val set: Set<Item?>? = null,
        val map: Map<String, Item?>? = null,
        val strings: Set<String?>? = null,
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
                    ),
                ),
            )

            result shouldBe Invalid(
                listOf(
                    ValidationError(path = "list[0].value", errorCode = "ITEM_VALUE_BLANK"),
                    ValidationError(path = "list[2].value", errorCode = "ITEM_VALUE_BLANK"),
                ),
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

            result shouldBe Invalid(
                listOf(
                    ValidationError(path = "list[1].value", errorCode = "ITEM_VALUE_BLANK"),
                ),
            )
        }
    }

    context("validateEach with a set") {
        val validator = buildValidator {
            TestedClass::set.validateEach {
                Item::value.isNotBlank(errorCode = "ITEM_VALUE_BLANK")
            }
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

            result shouldBe Invalid(
                listOf(
                    ValidationError(path = "set[0].value", errorCode = "ITEM_VALUE_BLANK"),
                ),
            )
        }
    }

//    context("nested validateEach") {
//        val validator = buildValidator {
//            TestedClass::nested.validate {
//                NestedClass::items.validateEach {
//                    Item::value.isNotBlank(errorCode = "ITEM_VALUE_BLANK")
//                }
//            }
//        }
//
//        should("preserve the parent path before the element index") {
//            val result = validator(
//                TestedClass(
//                    nested = NestedClass(
//                        items = listOf(Item("")),
//                    ),
//                ),
//            )
//
//            result shouldBe Invalid(
//                listOf(
//                    ValidationError(path = "nested.items[0].value", errorCode = "ITEM_VALUE_BLANK"),
//                ),
//            )
//        }
//    }

//    context("validateEach with nested validation") {
//        val validator = buildValidator {
//            TestedClass::nestedItems.validateEach {
//                NestedItem::item.validate {
//                    Item::value.isNotBlank(errorCode = "ITEM_VALUE_BLANK")
//                }
//            }
//        }
//
//        should("compose nested property paths after the element index") {
//            val result = validator(
//                TestedClass(
//                    nestedItems = listOf(
//                        NestedItem(Item("")),
//                    ),
//                ),
//            )
//
//            result shouldBe Invalid(
//                listOf(
//                    ValidationError(
//                        path = "nestedItems[0].item.value",
//                        errorCode = "ITEM_VALUE_BLANK",
//                    ),
//                ),
//            )
//        }
//    }

    context("validateValues") {
        val validator = buildValidator {
            TestedClass::map.validateValues {
                Item::value.isNotBlank(errorCode = "ITEM_VALUE_BLANK")
            }
        }

        should("include the map key in an invalid element path") {
            val result = validator(
                TestedClass(
                    map = linkedMapOf(
                        "first" to Item(""),
                        "second" to Item("valid"),
                    ),
                ),
            )

            result shouldBe Invalid(
                listOf(
                    ValidationError(path = "map[first].value", errorCode = "ITEM_VALUE_BLANK"),
                ),
            )
        }
    }

    context("eachSatisfies") {
        val validator = buildValidator {
            TestedClass::list.eachSatisfies(errorCode = "ITEM_VALUE_BLANK") {
                it?.value?.isNotBlank() == true
            }
        }

        should("treat null and empty collections as valid") {
            validator(TestedClass(list = null)) shouldBe Valid(TestedClass(list = null))
            validator(TestedClass(list = emptyList())) shouldBe
                Valid(TestedClass(list = emptyList()))
        }

        should("include the index in every invalid element path") {
            val result = validator(
                TestedClass(
                    list = listOf(
                        Item(""),
                        Item("valid"),
                        Item(" "),
                    ),
                ),
            )

            result shouldBe Invalid(
                listOf(
                    ValidationError(path = "list[0]", errorCode = "ITEM_VALUE_BLANK"),
                    ValidationError(path = "list[2]", errorCode = "ITEM_VALUE_BLANK"),
                ),
            )
        }
    }
})
