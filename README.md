# Supermarket checkout component
Finally two version of the implementations has been created.
- SafeShopCash
- FastShopCash

Both are extending same abstract class called *ShopCash*, which holds
common behaviour and dao object (from which we can get product information).
There is possibility to extend both implementations, but only we can add
new functionalities, already implemented methods has been set to final.

*SafeShopCash* for each *checkBill* call, recalculates products sum
based on data which are stored at this point inside the object,
which lead us to less sufficient solution in situation, when we have
a lot of *checkBill* calls and stored products.

In *FastShopCash*, method *checkBill* call is done much faster because
we are not recalculating the final sum each time. It is done when product
is scanned.

Very simply - for given case:
> User adds n same products and checks price after each scan

- for *FastShopCash* we will do n computations
- for *SafeShopCash* we will do n! computations

As you can see *FastShopCash* is much faster. The only disadvantage of
the first solution and the only reason why I have also created second one,
is that extending *FastShopCash* is much easier to break internal state,
because of the *sum* variable. For *SafeShopCash* this is almost impossible.

Imagine new functionality implementation for removing already scanned
products.

For *SafeShopCash* class the implementation might look like this:
```java
public void remveProcuct(String productCode) {
    int count = scannedProducts.getOrDefault(productCode, 1);
    scannedProducts.put(productCode, --count);
}
```
Easy and clean.


For *FastShopCash* it wont't be that simple, because we need to recalculate
*sum* variable and take *promotion* factor into account.

But still in my opinion *FastShopCash* is MUCH BETTER implementation for
most cases.


*SafeShopCash* we can use only in situation when we are going to
read the price only once, at the end of the scanning, or when person
which is going to add new functionality, is not aware of the danger of
breaking internal state of the object. (which is kind of paradox, because
to now that you can break internal state of the *FastShopCash* first you
need to understand how it works...)