<?php

namespace database_connector\controller\utilities;

/**
 * Utility class for the creation of HTML tables.
 */
class TableCreator
{
    /** @var int The number of columns in the table. */
    private $number_of_columns;
    /** @var string A string holding the HTML table. */
    private $table;

    /**
     * TableCreator constructor.
     * @param int $number_of_columns The number of columns in the table.
     */
    public function __construct(int $number_of_columns)
    {
        $this->number_of_columns = $number_of_columns;
        $this->table = "<table>";
    }

    /**
     * Set a "super-header" to the table.
     * @param array $columns The title for the columns in the super-header.
     * @param array $columns_sizes The size of the columns in the super-header (expressed in number of
     * "normal" columns).
     */
    public function set_super_header(array $columns, array $columns_sizes)
    {
        assert(count($columns) == count($columns_sizes) && array_sum($columns_sizes) == $this->number_of_columns);
        $this->table .= "<tr>";
        for ($i = 0; $i < count($columns); $i++) {
            $this->table .= "<th colspan='" . $columns_sizes[$i] . "'>" . $columns[$i] . "</th>";
        }
        $this->table .= "</tr>";
    }

    /**
     * Set the header of the table.
     * @param array $columns The columns' title for the header.
     */
    public function set_header(array $columns)
    {
        $this->add_row_to_table($columns, "th");
    }

    /**
     * Add a row to the table.
     * @param array $columns The column's data for the row.
     */
    public function add_row(array $columns)
    {
        $this->add_row_to_table($columns, "td");
    }

    /**
     * Get the full table.
     * @return string The HTML table source.
     */
    public function get_table(): string
    {
        return $this->table . "</table>";
    }

    /**
     * Add a generic row to the table.
     * @param array $columns The columns of the row.
     * @param string $type_of_cell The type of cell ("td" or "th").
     */
    private function add_row_to_table(array $columns, string $type_of_cell)
    {
        assert(count($columns) == $this->number_of_columns);
        $this->table .= "<tr>";
        foreach ($columns as $value) {
            $this->table .= "<" . $type_of_cell . ">" . $value . "</" . $type_of_cell . ">";
        }
        $this->table .= "</tr>";
    }
}
