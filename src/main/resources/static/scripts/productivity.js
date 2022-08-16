let table = document.getElementById("table");

table.querySelectorAll('th').forEach((th, position) => {
    th.addEventListener('click', () => sortTable(position + 1));
});

function compareValues(a, b) {
    return b.localeCompare(a);
}

function sortTable(column) {
    let rows = Array.from(table.querySelectorAll('tr'));

    rows = rows.slice(1);
    let qs = `td:nth-child(${column})`;

    rows.sort((r1, r2) => {
        let t1 = r1.querySelector(qs);
        let t2 = r2.querySelector(qs);

        return compareValues(t1.textContent, t2.textContent);
    });

    rows.forEach(row => table.appendChild(row));
}