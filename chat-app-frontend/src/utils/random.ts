const COLOR_LISTS = [
  '#f56a00',
  '#7265e6',
  '#ffbf00',
  '#00a2ae',
  '#00ae0e',
  '#9c35b1',
  '#0da887',
  '#0d77a8'
];

export const COLOR_LISTS_LENGTH = COLOR_LISTS.length;

export const randomColor = (index?: number): string =>
  COLOR_LISTS[index ? index : Math.floor(Math.random() * COLOR_LISTS.length)];
