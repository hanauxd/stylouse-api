<html>
    <head></head>
    <body>
        <p>Dear ${name},</p>
        <p>Your order has been confirmed.</p>
        <table style="background-color: floralwhite; padding: 20px">
            <tbody>
            <tr>
                <td style="font-weight: bold">Order Date</td>
                <td>${date}</td>
            </tr>
            <tr>
                <td style="font-weight: bold">Shipping to</td>
                <td>
                    <p>${address}</p>
                    <p>${city} (${postalCode})</p>

                </td>
            </tr>
            <tr>
                <td style="font-weight: bold">Payment method</td>
                <td>${paymentMethod}</td>
            </tr>
            <tr>
                <td style="font-weight: bold">Your purchase items:</td>
                <td>
                    <ul>
                        <#list orders as orderItem>
                            <li>${orderItem.quantity}x ${orderItem.size} ${orderItem.product.name}</li>
                        </#list>
                    </ul>
                </td>
            </tr>
            <tr>
                <td style="font-weight: bold">TOTAL</td>
                <td>LKR ${total}</td>
            </tr>
            </tbody>
        </table>
        <div>
            <p>Thank you</p>
            <p>Stylouse</p>
        </div>
    </body>
</html>